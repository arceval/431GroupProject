package controller;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JFrame;

import Game.*;
import constraints.Drawable;
import menus.ClientMenu;
import menus.HostMenu;
import menus.MainMenu;
import menus.ResultMenu;
import menus.WaitMenu;

//For changing game state ex: menu transitions
public class GameState extends Drawable{
	//port to listen, players must connect in this order
	public int[] port = {90,91,92,93,94,95,96,97,98,99};
	//Time (Synced with server)
	public Instant time;
	//Delay(calculated and by client after)
	public long delay = 0;
	//Host Flag
	public boolean Host = false;
	//Host IP
	public String hostIP;
	//Local Player Count, incremented when local players join
	public int localPlayerCount = 1;
	//Ids of players, Initialized when everybody is connected
	public ArrayList<Player> players;
	//My ID
	public String myPlayerID = "";
	//The 3 other players
	public int numberOfPlayers = 3;

	//Territory limit of a square before it belongs to a player: in percentage out of 100 
	public String territoryLimit = "30";
	//Pen Thickness in pixels
	public String penThickness = "6";
	//lock timeout in ms
	public final int timeoutMS = 10000;
	//Size of Game Window
	public final int width = 503;
	public final int height = 525;
	private JFrame frame;
	public static Canvas window;
	//create the keyboard
	private Keyboard keyboard = new Keyboard(this);
	//List of connections
	public ArrayList<ConnectionEndpoint> connections = new ArrayList<ConnectionEndpoint>();

	//Available States
	private MainMenu mainMenu = new MainMenu();
	private	GameBoard gameBoard = new GameBoard(this);
	private HostMenu hostMenu;
	private ClientMenu clientMenu;
	private ResultMenu resultMenu;
	private WaitMenu waitMenu = new WaitMenu();
	//Available States
	private ArrayList<String> availableStates;
	//current state
	public static String currentState = "MainMenu";
	
	public GameState() {
		//initial setup
		availableStates = new ArrayList<String>();
		players = new ArrayList<Player>();
		hostMenu = new HostMenu(this,gameBoard);
		clientMenu = new ClientMenu(this,gameBoard);
		resultMenu = new ResultMenu(this,gameBoard);
		//set states here
		availableStates.add("MainMenu");
		availableStates.add("GameBoard");
		availableStates.add("HostMenu");
		availableStates.add("ClientMenu");
		availableStates.add("WaitMenu");
		availableStates.add("ResultMenu");


	}
	public void sendMessage(Message message) {
		//check if host or client
		//Host
		if(message.getSourceIpAddress().equals(hostIP.toString())) {
			boolean dropMessage = false;
			//modify message if its declare to update
			if(message.getAction().equals("declare")) {
				//debug message
				System.out.println("Host: sent update message to clients");
					message.setAction("update");
					//update own board
					gameBoard.getSquareList().get(message.getSquareID()).setOccupied(true);
					gameBoard.getSquareList().get(message.getSquareID()).setOccupiedPlayerID(message.getPlayerID());
			}else if(message.getAction().equals("check")) {
				//debug message
				System.out.println("Host: sent lock message to clients");
				//modify a check
				//check if that square is locked
				if(!gameBoard.getSquareList().get(message.getSquareID()).isLocked()) {
					//the square is free, send an update to everyone your taking it
					message.setAction("lock");
					//approve drawing for square
					gameBoard.getSquareList().get(message.getSquareID()).setApproved(true);
				}else {
					//drop this message
					System.out.println("Host: No need to send, dropping lock message");
					dropMessage = true;
				}
			}else if(message.getAction().equals("release")) {
				//debug message
				System.out.println("Host: sent unlock message from clients");
				//modify a release to unlock
				message.setAction("unlock");
			}
			if(!dropMessage) {
				//Send your update to everyone if message was not dropped
				for(int i = 0; i < connections.size(); i++) {
					connections.get(i).sendMessage(message);
				}
			}

		}else {
			//Client send your update to the host
			//debug message
			System.out.println("Client: sent " + message.getAction() +" message to Host");
			connections.get(0).sendMessage(message);
		}
	}

	
	
	public void setCanvas(Canvas canvas) {
		this.window = canvas;
	}
	public void init() {
		//set the canvas size
		window.setSize(width, height);
		//set canvas background
		window.setBackground(Color.white);
		
		//add Mouse listener 
		//MainMenu
		window.addMouseListener(mainMenu);
		//GameBoard
		window.addMouseListener(gameBoard);
		window.addMouseMotionListener(gameBoard);
		//HostMenu
		window.addMouseListener(hostMenu);
		//ClientMenu
		window.addMouseListener(clientMenu);
		//add keyboard listener
		window.addKeyListener(keyboard);
		//empty frame for the canvas to be placed in
		frame = new JFrame();
		//close the program when window is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//set the frame size
		frame.setSize(width, height);
		//disable resizing of frame
		frame.setResizable(false);
		//place the panel into the frame
		frame.add(window);
		//make both frame and panel visible
		frame.setVisible(true);
		window.setVisible(true);

	}
	public void setCurrentState(String s) {
		currentState = s;
	}
	public Canvas getCanvas() {
		return this.window;
	}
	//getter for player list
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
	public void render(Graphics2D g) {
		switchMethod("render", g);
	}
	
	//For client and Host (Menu Switching + Reading received Messages from endpoint)
	public void tick() {
		//debug
//		System.out.println("TerritoryReq: " + this.territoryLimit);
//		System.out.println("PenThickness " + this.penThickness);
		switchMethod("tick", null);
		//Constantly read received messages from endpoints
		for(ConnectionEndpoint connection : connections) {
			Message message = connection.readMessage();
			if(message != null) {
				//Debugging
				System.out.println(myPlayerID + ": received message, syncMsg: " + message.isForSync());
			}
		}
	}
	
	private void switchMethod(String action, Graphics2D g) {
		//Render
		if(action == "render") {
			switch(currentState) {
			
			case "MainMenu":
				currentState = "MainMenu";
				mainMenu.render(g);
				break;
			case "GameBoard":
				currentState = "GameBoard";
				gameBoard.render(g);
				break;
			case "HostMenu":
				currentState = "HostMenu";
				hostMenu.render(g);
				break;
			case "ClientMenu":
				currentState = "ClientMenu";
				clientMenu.render(g);
				break;
			case "WaitMenu":
				currentState = "WaitMenu";
				waitMenu.render(g);
				break;
			case "ResultMenu":
				currentState = "ResultMenu";
				resultMenu.render(g);
				break;
				
			default: 
				currentState = "MainMenu";
				break;
			}
			
		}
		
		//Tick
		
		else if(action == "tick") {
			switch(currentState) {
			
			case "MainMenu":
				currentState = "MainMenu";
				mainMenu.tick();
				break;
			case "GameBoard":
				currentState = "GameBoard";
				gameBoard.tick();
				break;
			case "HostMenu":
				currentState = "HostMenu";
				hostMenu.tick();
				break;
			case "ClientMenu":
				currentState = "ClientMenu";
				clientMenu.tick();
				break;
			case "WaitMenu":
				currentState = "WaitMenu";
				waitMenu.tick();
				break;
			case "ResultMenu":
				currentState = "ResultMenu";
				resultMenu.tick();
				break;
				
			default: 
				currentState = "MainMenu";
				break;
			}
		}

	}


	//return server socket
	public ServerSocket getServerSocket() {
		return hostMenu.getServerSocket();
	}

	public void setGameState(String state) {
		if(availableStates.contains(state)) {
			currentState = state;
		}else {
			System.out.println(state + " is an unavailable state");
		}
	}
}
