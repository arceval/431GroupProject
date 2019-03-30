package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
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
	//size of square
	private int width;
	private int height;
	//position of square
	private int posX;
	private int posY;
	//occupied flag
	private boolean occupied = false;
	//Built after number of players are known
	public Square(int numPlayers, float territoryLimit, float penThickness, int posX, int posY, int sizeOfSquareSide) {
		//set number of players
		this.numPlayers = numPlayers;

		//for each player ID found in the playerIDs list
		for(String s : GameState.playerIDs) {
			//add their id to the square and their territory amount to 0
			status.put(s, 0.0f);
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
		for(String playerId : GameState.playerIDs) {
			if(status.get(playerId) >= GameState.territoryLimit) {
				occupied = true;
			}
		}
	}
	
	public void drawSquare(Graphics2D g) {
		//set painter color to black
		g.setColor(Color.black);
		//Paint the square
		g.draw(square);
	}

	@Override
	public void render(Graphics2D g) {
		//draw this square
		drawSquare(g);
		
	}

	@Override
	public void tick() {
		//update occupancy if not occupied yet
		if(!occupied) {updateOccupancy();}
	}

}
