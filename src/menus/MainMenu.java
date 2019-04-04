package menus;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import constraints.Drawable;
import controller.GameState;

public class MainMenu extends Drawable implements MouseListener{
	//Image for main menu
	private BufferedImage mainMenuImage;
	//Join and host button
	private Shape joinButton;
	private Shape hostButton;
	//Main menu constructor
	public MainMenu() {
		try {

			//set image
			mainMenuImage = ImageIO.read(new File("res/main_menu.jpg"));
			//Create buttons
			//Join button
			joinButton = new Rectangle2D.Double(188, 165, 110, 50);
			//Host Button
			hostButton = new Rectangle2D.Double(185, 230, 118, 43);
		}catch(IOException e) {
			System.out.println("Failed to open mainMenuImage located in res folder");
			e.printStackTrace();
		}
	}

	
	
	public void render(Graphics2D g) {
		//Draw the main menu
		g.drawImage(mainMenuImage, null, 0, 0);
		
//		//Join button
//		g.drawRect(188, 165, 110, 50);
//		//Host Button
//		g.drawRect(185, 230, 118, 43);


	}



	@Override
	public void tick() {
		
	}


	//For registering clicks
	@Override
	public void mouseClicked(MouseEvent e) {
		//make sure we only listen when we're at the main menu
		if(GameState.currentState.equals("MainMenu")) {
			//Join Button clicked
			if(joinButton.contains(e.getPoint())){
				System.out.println("MainMenu: Switching to Join Menu");
				//change to Join Menu
				GameState.currentState = "ClientMenu";
				//Host button clicked
			}else if(hostButton.contains(e.getPoint())) {
				System.out.println("MainMenu: Switching to Host Menu");
				//change to Host Menu
				GameState.currentState = "HostMenu";

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
	

}
