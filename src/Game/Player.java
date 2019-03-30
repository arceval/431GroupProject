package Game;

public class Player {
	//Player Id
	String playerId = "";
	//player color
	String playerColor = "";

	public Player(String playerID, String color) {
		this.playerId = playerID;
		this.playerColor = color;
	}
	
	public String getPlayerID() {return this.playerId;}
}
