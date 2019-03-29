package menus;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainMenu{

	private BufferedImage mainMenuImage;
	//To be called when program runs
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
		g.drawImage(mainMenuImage, null, 0, 0);
	}
	
	public void tick() {
		
	}
}
