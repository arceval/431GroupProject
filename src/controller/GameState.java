package controller;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JFrame;

import constaints.Drawable;
import menus.MainMenu;

//For changing game state ex: menu transitions
public class GameState extends Drawable{
	//Ids of players
	public static ArrayList<String> playerIDs = new ArrayList<String>();
	//The 3 other players
	public static int numberOfPlayers = 3;
	//Territory limit of a square before it belongs to a player: in percentage out of 100 
	public static float territoryLimit = 25;
	//Pen Thickness
	public static float penThickness = 1;
	//Size of Game Window
	public final int width = 505;
	public final int height = 530;
	private JFrame frame;
	private Canvas window;
	
	//Available classes
	MainMenu mainMenu = new MainMenu();

	//Available States
	private ArrayList<String> availableStates;
	//current state
	private String currentState = "MainMenu";
	
	public GameState() {
		availableStates = new ArrayList<String>();
		//set states here
		availableStates.add("MainMenu");
	}
	public void setCanvas(Canvas canvas) {
		this.window = canvas;
	}
	public void init() {
		//set the canvas size
		window.setSize(width, height);
		//set canvas background
		window.setBackground(Color.black);
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
	public Canvas getCanvas() {
		return this.window;
	}
	public void render(Graphics2D g) {
		switchMethod("render", g);
	}
	
	public void tick() {
		switchMethod("tick", null);

	}
	
	private void switchMethod(String action, Graphics2D g) {
		//Render
		if(action == "render") {
			switch(currentState) {
			
			case "MainMenu":
				currentState = "MainMenu";
				mainMenu.render(g);
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
			
			default: 
				currentState = "MainMenu";
				break;
			}
			
		}

	}
	
	public void setGameState(String state) {
		if(availableStates.contains(state)) {
			currentState = state;
		}else {
			System.out.println(state + " is an unavailable state");
		}
	}
}
