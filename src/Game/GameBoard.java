package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import constraints.Drawable;
import controller.GameState;
import controller.Message;

public class GameBoard extends Drawable implements MouseListener, MouseMotionListener{

	//Individual Squares
	private ArrayList<Square> squareList = new ArrayList<Square>();
	//current square interaction
	private int currentSquareIndex = -1;
	//number of Rows
	private final int rows = 16;
	//number of Columns
	private final int columns = 16;
	private final double pi = 3.14159265359;
	//size of squares in pixels
	private int sizeOfSquares = 31;
	//Pen shape for drawing
    private double radius;
    //pen History
    private ArrayList<Shape> penHistory = new ArrayList<Shape>();
    //our player color
    private Color myColor;
    private double diameter;
	private GameState GameState;
	private LinkedHashMap<Color, Integer> results = new LinkedHashMap<Color,Integer>();
	public GameBoard(GameState GameState) {
		//set gamestate ref
		this.GameState = GameState;
		//populate board
		int squareID = 0;
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < columns; y++) {
				//add square to squareList
				squareList.add(new Square(GameState,GameState.numberOfPlayers, squareID, GameState.territoryLimit, GameState.penThickness, sizeOfSquares*x, sizeOfSquares*y, sizeOfSquares));
				squareID++;
			}
		
		}
		//set pen size
		diameter = Float.valueOf(GameState.penThickness);
		radius = diameter/2d;


	}
	@Override
	public void render(Graphics2D g) {
		Color origColor = g.getColor();
		//for each square in the gameboard
		for(Square square : squareList) {
			//render the square
			square.render(g);
		}
		//clone the pen history
		ArrayList<Shape> penHistoryClone = (ArrayList) penHistory.clone();
		//check pen history
		if(!penHistoryClone.isEmpty()) {
			for(Shape s : penHistoryClone) {
				//set painter color to own color
				g.setColor(myColor);
				//render each point in pen history
				g.fill(s);
			}
		}
		//set painter back to original color
		g.setColor(origColor);
	}

	@Override
	public void tick() {
		//check if game is over
		boolean gameIsNotOver = false;
		for(Square square : getSquareList()) {
			if(!square.isOccupied()) {
				gameIsNotOver = true;
				break;
			}
		}
		//game over detected
		if(!gameIsNotOver) {
			
			//tally results
			for(Square square : getSquareList()) {
				//check if first time seeing color 
				//*note there is one white square outside the window, ignore this square
				if(results.get(square.getOccupiedPlayerColor())==null && !square.getOccupiedPlayerColor().equals(Color.white)) {
					results.put(square.getOccupiedPlayerColor(), 1);
				}else {
					//error check white color
					if(square.getOccupiedPlayerColor().equals(Color.white)) {
						
					}else {
						//color exists, increment by 1
						results.put(square.getOccupiedPlayerColor(), results.get(square.getOccupiedPlayerColor())+1);
					}

				}
			}
			//Change to results menu and display results
			GameState.setCurrentState("ResultMenu");
		}
		
		//set own player color is not set yet
		if(myColor==null) {
			//set playerColor
			for(Player player : GameState.getPlayers()) {
				if(player.getPlayerID().equals(GameState.myPlayerID)) {
					myColor = player.getPlayerColor();
					break;
				}
			}
		}
		for(Square square : getSquareList()) {
			square.tick();
		}
	}
	//getter for results list
	public LinkedHashMap<Color, Integer> getResults(){
		return this.results;
	}
	//getter for square list
	public ArrayList<Square> getSquareList(){
		return this.squareList;
	}
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(GameState.currentState.equals("GameBoard")) {
			//For each square in the gameboard
			for(Square square : squareList) {
				//check if it was the one clicked
				if(square.isClicked(e)) {
					System.out.println("Square Id clicked: " + square.getSquareID());
					//The square has been clicked
					//get the square id and ask gameState->connectionEndpoint->Host if this square is not locked
					GameState.sendMessage(new Message(GameState.time.toString(),GameState.myPlayerID,square.getSquareID(),"check",GameState.myPlayerID));
					//update current square interaction
					currentSquareIndex = square.getSquareID();
					// Assume we will get approved and start drawing
					if(squareList.get(currentSquareIndex).getShape().contains(e.getPoint())) {
						Shape shapeToAdd = new Ellipse2D.Double(e.getX() - radius, e.getY() - radius, diameter, diameter);
						//check if point does not exist already
						if(!penHistory.contains(shapeToAdd)) {
							penHistory.add(shapeToAdd);
						}else {
							//debug message
	//						System.out.println("Ignored point");
						}
					}
		
				}
			}
		
		}
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(GameState.currentState.equals("GameBoard")) {
			//check if you had approval status
			if(getSquareList().get(currentSquareIndex).isApproved()) {
				//remove your approval status
				getSquareList().get(currentSquareIndex).setApproved(false);
				//check if player covered enough space
				if(!penHistory.isEmpty()) {
					//total pixel coverage = 3.14*r^2
					//minimizing factor for correction pixel area of points
					float minFactor = Float.valueOf(GameState.penThickness)*0.014f;
					System.out.println("Client: penHistory size: " + penHistory.size());
					int pixelCoverage = (int) (pi*radius*radius*(penHistory.size()*minFactor));
					int requiredPixelAmount = (int)((Float.valueOf(GameState.territoryLimit)/100f)*getSquareList().get(currentSquareIndex).getPixelCount());
					System.out.println("Client: pixelsCovered " + pixelCoverage + " Req Pixels: " + requiredPixelAmount);
					//check coverage with amount needed
					if(pixelCoverage >= requiredPixelAmount) {
						//Pixel Coverage is enough to declare territory
						//send a declare message
						System.out.println("Client: sent a declare message to host");
						GameState.sendMessage(new Message(GameState.time.now().toString(), GameState.myPlayerID, currentSquareIndex, "declare", GameState.myPlayerID));
						//clear pen history
						penHistory.clear();
						//clear currentSquare
						currentSquareIndex = -1;
					}else {
						System.out.println("Client: Not enough coverage");
						//send a release message
						System.out.println("Client: sent a release message to host");
						GameState.sendMessage(new Message(GameState.time.now().toString(), GameState.myPlayerID, currentSquareIndex, "release", GameState.myPlayerID));
						//Pixel Coverage was insignificant
						System.out.println("Client: clearing pen history");
						//clear pen history
						penHistory.clear();
						//clear currentSquare
						currentSquareIndex = -1;
					}
				}
			}else {
				//had no approval
				//clear pen history
				for(int i = 0; i < penHistory.size(); i++) {
					penHistory.remove(i);
				}
				//clear currentSquare
				currentSquareIndex = -1;
			}
		
		}else {
			penHistory.clear();
			currentSquareIndex = -1;
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if(GameState.currentState.equals("GameBoard")) {
			//if square is approved update shape
			if(squareList.get(currentSquareIndex).isApproved()) {
				//its ok to draw add mouse point as a circle into arraylist if it intersects with the current Square
				if(squareList.get(currentSquareIndex).getShape().contains(e.getPoint())) {
					Shape shapeToAdd = new Ellipse2D.Double(e.getX() - radius, e.getY() - radius, diameter, diameter);
					//check if point does not exist already
					if(!penHistory.contains(shapeToAdd)) {
						penHistory.add(shapeToAdd);
					}else {
						//debug message
//						System.out.println("Ignored point");
					}
				}

			}
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}
