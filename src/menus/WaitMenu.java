package menus;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import constraints.Drawable;

public class WaitMenu extends Drawable{
	//Image for Wait Menu
	private BufferedImage WaitMenu;
	
	public WaitMenu() {
		try {
			//set image
			WaitMenu = ImageIO.read(new File("res/WaitMenu.jpg"));


		}catch(IOException e) {
			//for failure in opening image
			System.out.println("Failed to open waitMenuImage located in res folder");
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		//Draw the Client wait menu
		g.drawImage(WaitMenu, null, 0, 0);		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
