package Game;

import java.util.ArrayList;
import java.util.HashMap;
//A square of the game board
public class Square {
	//Number of Players
	private int numPlayers;
	//List of player IDs
	private ArrayList<String> playerIDs = new ArrayList<String>();
	//Pen Thickness
	private float penThickness;
	//Amount Filled status (each index holds a players current territory)
	private HashMap<String, Float> status = new HashMap<String, Float>();
	//Area of territory in square before taken over
	private float territoryLimit;
	
	//Built after number of players are known
	public Square(ArrayList<String> playerIDs, int numPlayers, float territoryLimit, float penThickness) {
		//set number of players
		this.numPlayers = numPlayers;
		//set the player IDs to local list
		this.playerIDs = playerIDs;
		//set penThickness of players
		this.penThickness = penThickness;
		//for each player ID found in the playerIDs list
		for(String s : playerIDs) {
			//add their id to the square and their territory amount to 0
			status.put(s, 0.0f);
		}
		//set territory limit of the square
		this.territoryLimit = territoryLimit;
			
		
	}
	
	//Getter for Square Status
	public HashMap<String, Float> getStatus(){
		return status;
	}
	
	//Add to status
	public void addTerritory(String playerID) {
		//Add territory to this square corresponding to playerID of amount penThickness 
		status.replace(playerID, penThickness);
	}

}
