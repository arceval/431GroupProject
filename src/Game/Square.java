package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import constraints.Drawable;
import controller.GameState;
//A square of the game board
public class Square extends Drawable{	
	public class timeOutTask extends TimerTask{

		public void run() {
			//release lock
			lock = false;
			//cancel timer
			timeoutTimer.cancel();
			timeoutTimer = null;
			//debug
			System.out.println("Square: timeout occured, lock released squareID: " + squareID);
		}
	}
	//Number of Players
	private int numPlayers;

	private Shape square;
	//used to refer to this square
	private int squareID;
	//size of square
	private int width;
	private int height;
	//pixel count
	private int pixelCount;
	//position of square
	private int posX;
	private int posY;
	//occupied flag and player
	private boolean occupied = false;
	private String occupiedPlayerID;
	//Default color
	private Color occupiedPlayerColor = Color.white;
	//lock flag
	private boolean lock = false;
	//GameState
	private GameState gameState;
	//Approved to draw
	private boolean approved = false;
	//timer for timeout
	private Timer timeoutTimer;
	//Built after number of players are known
	public Square(GameState gameState ,int numPlayers, int squareID, String territoryLimit, String penThickness, int posX, int posY, int sizeOfSquareSide) {
		//set gamestate
		this.gameState = gameState;
		//set number of players
		this.numPlayers = numPlayers;
		//set the square id
		this.squareID = squareID;

		//set position of square
		this.posX = posX;
		this.posY = posY;
		//set size of square
		this.width = sizeOfSquareSide;
		this.height = sizeOfSquareSide;
		pixelCount = this.width*this.height;
		square =  new Rectangle2D.Double(posX, posY, width, height);
	}

	public boolean isLocked() {
		return lock;
	}
	public void setLock(boolean flag) {
		this.lock = flag;
	}

	//Setter for approved response from host
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	//check if approved
	public boolean isApproved() {
		return this.approved;
	}


	//was clicked
	public boolean isClicked(MouseEvent e) {
		if(square.contains(e.getPoint())) {
			return true;
		}else {
			return false;
		}
	}
	public void drawSquare(Graphics2D g) {
		//set painter color to black
		g.setColor(Color.black);
		//Paint the square
		g.draw(square);
	}


	
	//get square ID
	public int getSquareID() {
		return this.squareID;
	}
	//set occupied flag
	public void setOccupied(boolean flag) {
		this.occupied = flag;
	}
	//get occupied flag
	public boolean isOccupied() {return this.occupied;}
	
	//set occupied id
	public void setOccupiedPlayerID(String s) {
		this.occupiedPlayerID = s;
	}
	//get occupied color
	public Color getOccupiedPlayerColor() {
		return this.occupiedPlayerColor;
	}
	//get occupied id
	public String getOccupiedPlayerID() {
		return this.getOccupiedPlayerID();
	}
	//getter for number of total pixels of square
	public int getPixelCount() {
		return this.pixelCount;
	}

	//getter for shape
	public Shape getShape() {
		return this.square;
	}
	@Override
	public void render(Graphics2D g) {
		//fill if occupied
		if(occupied) {
			g.setColor(occupiedPlayerColor);
			g.fill(square);
			//draw square boarder again
		}
		if(lock && !occupied){
			g.setColor(Color.gray);
			g.fill(square);
		}
		drawSquare(g);
	
	}

	@Override
	public void tick() {
		//if occupied lock
		if(occupied) {
			//lock this square
			setLock(true);
			//change color to occupied color
			for(Player player : gameState.getPlayers()) {
				if(player.getPlayerID().equals(occupiedPlayerID)) {
					//this player is the occupying player
					occupiedPlayerColor = player.getPlayerColor();
					break;
				}
			}
		}else if(lock) {
			//check if timeout timer has started already
			if(timeoutTimer == null) {
				//start timeout task
				timeoutTimer=new Timer();
				timeoutTimer.schedule(new timeOutTask(), gameState.timeoutMS);
			}

			
		}else {
			//cancel timeout thread if any
			if(timeoutTimer != null) {
				timeoutTimer.cancel();
				timeoutTimer = null;
			}
		}

	}

}
