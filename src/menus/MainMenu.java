package menus;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import constraints.Drawable;

public class MainMenu extends Drawable{
	//Image for main menu
	private BufferedImage mainMenuImage;

	public MainMenu() {
		try {

			//set image
			mainMenuImage = ImageIO.read(new File("res/main_menu.jpg"));
			
		}catch(IOException e) {
			System.out.println("Failed to open mainMenuImage located in res folder");
			e.printStackTrace();
		}
	}

	
	
	public void render(Graphics2D g) {
		//Draw the main menu
		g.drawImage(mainMenuImage, null, 0, 0);
	}



	@Override
	public void tick() {
		
	}
	

}
