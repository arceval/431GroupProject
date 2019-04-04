package menus;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JTextField;

import Game.Client;
import Game.GameBoard;
import Game.Host;
import constraints.Drawable;
import controller.GameState;

public class ClientMenu extends Drawable implements MouseListener{
	//Image for Client Menu
	private BufferedImage clientMenuImage;
	//gameState
	private GameState GameState;
	//Back Button
	private Shape backButton;
	//Ip TextField
	public static String ipInput = "";
	//client class
	private Client client;
	//flag for when Entered was pressed on keyboard
	public static boolean enter = false;
	private GameBoard gameBoard;
	public ClientMenu(GameState GameState, GameBoard gameBoard) {
		this.GameState = GameState;
		this.gameBoard = gameBoard;
		//client object for actual connecting
		client = new Client(this.GameState, this.gameBoard);
		try {
			//set image
			clientMenuImage = ImageIO.read(new File("res/ClientMenu.jpg"));
			//set back button
			backButton = new Rectangle2D.Double(9, 8, 40, 25);

		}catch(IOException e) {
			System.out.println("Failed to open mainMenuImage located in res folder");
			e.printStackTrace();
		}
	}
	//mouse detection
	@Override
	public void mouseClicked(MouseEvent e) {
		//Listen only if its Client menu rendered
		if(GameState.currentState.equals("ClientMenu")) {
			if(backButton.contains(e.getPoint())) {
				//Go back to main Menu
				System.out.println("ClientMenu: Switching to Main Menu");
				GameState.currentState = "MainMenu";
			}
		}		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	//render method for drawing menu
	@Override
	public void render(Graphics2D g) {
		//Draw the Client menu
		g.drawImage(clientMenuImage, null, 0, 0);
		//Draw back button
//		g.drawRect(9, 8, 40, 25);
		//print input ip field
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString(ipInput, 174, 300);
	}
	//tick method for logic
	@Override
	public void tick() {
		//Enter was pressed
		if(enter) {
			//init the client
			client.init();
			//change menu to wait menu
			GameState.currentState = "WaitMenu";
			//reset flag
			enter = false;
		}
		
	}

}
