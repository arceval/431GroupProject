package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import constraints.Drawable;
import controller.GameState;
//A square of the game board
public class Square extends Drawable{	
	//Number of Players
	private int numPlayers;


	//Amount Filled status (each index holds a players current territory)
	private HashMap<String, Float> status = new HashMap<String, Float>();
	private Shape square;
	//used to refer to this square
	private int squareID;
	//size of square
	private int width;
	private int height;
	//position of square
	private int posX;
	private int posY;
	//occupied flag
	private boolean occupied = false;
	//Built after number of players are known
	public Square(int numPlayers, int squareID, float territoryLimit, float penThickness, int posX, int posY, int sizeOfSquareSide) {
		//set number of players
		this.numPlayers = numPlayers;
		//set the square id
		this.squareID = squareID;
		//for each player declared by the server 
		for(Player player : GameState.players) {
			//add their id to the square and their territory amount to 0
			status.put(player.getPlayerID(), 0.0f);
		}

		//set position of square
		this.posX = posX;
		this.posY = posY;
		//set size of square
		this.width = sizeOfSquareSide;
		this.height = sizeOfSquareSide;
		square =  new Rectangle2D.Double(posX, posY, width, height);
	}

	//Getter for Square Status
	public HashMap<String, Float> getStatus(){
		return status;
	}
	
	//Add to status
	public void addTerritory(String playerID) {
		//ask the 
		//Add territory to this square corresponding to playerID of amount penThickness 
		status.replace(playerID, GameState.penThickness);
	}
	
	//update square occupancy
	public void updateOccupancy(){
		for(Player player : GameState.players) {
			if(status.get(player.getPlayerID()) >= GameState.territoryLimit) {
				occupied = true;
			}
		}
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

	public void drawSquareStatus(Graphics2D g) {
		if(occupied) {
			
		}
		for(Player player : GameState.players) {
			if(status.get(player.getPlayerID()) >= GameState.territoryLimit) {
				occupied = true;
			}
		}
	}
	
	//get square ID
	public int getSquareID() {
		return this.squareID;
	}
	@Override
	public void render(Graphics2D g) {
		//draw this square
		drawSquare(g);
		
		//draw its current status
		
	}

	@Override
	public void tick() {
		//update occupancy if not occupied yet
		if(!occupied) {updateOccupancy();}
	}

}
