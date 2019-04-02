package Game;

import java.awt.Color;
import java.io.Serializable;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6507820150025537425L;
	//Player Id (The ip)
	String playerId = "";
	//player color
	Color playerColor;

	public Player(String playerID, Color color) {
		this.playerId = playerID;
		this.playerColor = color;
	}
	
	public String getPlayerID() {return this.playerId;}
	
	public Color getPlayerColor() {return this.playerColor;}
}
