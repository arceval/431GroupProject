package controller;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

import Game.Player;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4934691994355134807L;
	//For initial syncing
	private boolean forSync = false;
	private String serverTime;
	private ArrayList<Player> playerList;
	private String hostAddress;
	private int clientsPlayerIDIndex;
	
	
	//For gameplay
	private String timeStamp = "";
	private String playerID = "";
	private String action = "";
	private int squareID;
	private String sourceIpAddress;
	
	//For debugging
	private String debugMsg = "";
	
	/*
	 Actions Host:
		lock - Lock a square
		update - Update square occupancy
		unlock - unlock a square
		approve - response to a check
		deny - response to a check
	Actions Client:
		check - check if square is free
		release - tell host you released
		declare - declare you took this square
	*/
	//Messages
		//Message format timestamp/playerID/action/SquareInfo
		//Square info format: SquareID: PlayerID: Player status (Territory in percentage)
	//For Both Client and Host
	public Message(String timestamp, String playerID, int squareID,String action, String sourceIpAddress) {
		this.timeStamp = timestamp;
		this.playerID = playerID;
		this.squareID = squareID;
		this.action = action;
		this.sourceIpAddress = sourceIpAddress;
	}
	//For debugging
	public Message(String s) {
		debugMsg = s;
	}
	//init message sent by host for syncing
	public Message(String serverTime, ArrayList<Player> playerList, String hostAddress, int clientsPlayerID) {
		this.serverTime = serverTime;
		this.playerList = playerList;
		this.hostAddress = hostAddress;
		//in sync with playerList array
		this.clientsPlayerIDIndex = clientsPlayerID;
		this.forSync = true;
	}
	
	//init message sent by host for syncing after player (not host) disconnects
	public Message(String serverTime, ArrayList<Player> playerList, String hostAddress) {
		this.serverTime = serverTime;
		this.playerList = playerList;
		this.hostAddress = hostAddress;
		//set sync flag
		this.forSync = true;
	}

	//setters
	public void setAction(String action) {this.action = action;}
	// Getters
	public String getTimeStamp() {return this.timeStamp;}
	public String getPlayerID() {return this.playerID;}
	public String getAction() {return this.action;}
	public int getSquareID() {return this.squareID;}
	public String getSourceIpAddress() {return this.sourceIpAddress;}
	public boolean isForSync() {return this.forSync;}
	public String getServerTime() {return this.serverTime;}
	public ArrayList<Player> getPlayerList() {return this.playerList;}
	public String getHostAddress() {return this.hostAddress;}
	public int getClientsPlayerIDIndex() {return this.clientsPlayerIDIndex;}
	public String getDebugMessage() {return this.debugMsg;}

}	

