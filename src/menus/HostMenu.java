package menus;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import javax.imageio.ImageIO;

import Game.GameBoard;
import Game.Host;
import Game.Player;
import constraints.Drawable;
import controller.GameState;

public class HostMenu extends Drawable implements MouseListener{
	//Image for Host Menu
	private BufferedImage hostMenuImage;
	//Back Button
	private Shape backButton;
	//My ip
	private String myIp;
	//Host class
	private Host host;
	private boolean hostHasBeenInit = false;
	private boolean hasRendered = false;
	//GameState
	private GameState GameState;
	private GameBoard gameBoard;
	public HostMenu(GameState GameState, GameBoard gameBoard) {
		this.GameState = GameState;
		this.gameBoard = gameBoard;
		
		try {
			//for host capabilities
			 host = new Host(GameState, gameBoard);
			//set image
			hostMenuImage = ImageIO.read(new File("res/HostMenu.jpg"));
			//set back button
			backButton = new Rectangle2D.Double(11, 8, 37, 25);
			//fix ip
			String[] split = InetAddress.getLocalHost().toString().split("/");

			//display ip
			if(split.length>1) {myIp=split[1];}else {myIp=split[0];}
			
		}catch(IOException e) {
			System.out.println("Failed to open mainMenuImage located in res folder");
			e.printStackTrace();
		}
	}
	//getter for socket
	public ServerSocket getServerSocket() {
		return host.getServerSocket();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(GameState.currentState.equals("HostMenu")) {
			if(backButton.contains(e.getPoint())) {
				//Cancel wait
				Host.abort = true;
				//undo host init
				hostHasBeenInit = false;
				//undo render check flag
				hasRendered = false;
				//Go back to main Menu
				System.out.println("HostMenu: Switching to Main Menu");
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

	@Override
	public void render(Graphics2D g) {
		//draw menu
		g.drawImage(hostMenuImage, null, 0, 0);
		//Back Button
//		g.drawRect(11, 8, 37, 25);
		//Ip Field
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString(myIp, 168, 300);
		//Territory
		g.drawString(GameState.territoryLimit, 235, 145);
		//Pensize
		g.drawString(GameState.penThickness, 240, 200);
		//Pensize
		if(!hasRendered) {hasRendered=true;}
		//render each player as they connect
		//spacing in px
		int spacingBtwnNames = 25;
		//current index of player
		int currentCount = 0;
		//for every player in the current player list
		for(Player player : GameState.getPlayers()) {
			//render their player tags
			g.setFont(new Font("Arial", Font.BOLD, 17));
			g.setColor(player.getPlayerColor());
			g.drawString(player.getPlayerID(), 170, 420+(spacingBtwnNames*currentCount));
			currentCount++;
		}
	}
//for logic updates
	@Override
	public void tick() {
		//Perform Host if first run
		if(!hostHasBeenInit && hasRendered) {hostHasBeenInit=true;host.init();}

	}

}
