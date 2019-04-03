package menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;

import Game.GameBoard;
import Game.Player;
import constraints.Drawable;
import controller.GameState;

public class ResultMenu extends Drawable{
	//Image for Result menu
	private BufferedImage resultMenuImage;
	//for passed in reference
	private GameBoard gameBoard;
	private LinkedHashMap<Color, Integer> results;
	//winners and their values
	private ArrayList<Color> winners;
	private ArrayList<Integer> values = new ArrayList<Integer>();
	private boolean inited = false;
	private GameState gameState;
	public ResultMenu(GameState gameState,GameBoard gameBoard) {
		this.gameBoard = gameBoard;
		this.gameState = gameState;
		try {
			//set image
			resultMenuImage = ImageIO.read(new File("res/ResultMenu.jpg"));
			winners = new ArrayList<Color>();
			//init first 4 indexes
			winners.add(null);
			winners.add(null);
			winners.add(null);
			winners.add(null);
		}catch(IOException e) {
			System.out.println("Failed to open ResultMenu located in res folder");
			e.printStackTrace();
		}
	}
	@Override
	public void render(Graphics2D g) {
		//Draw the result menu
		g.drawImage(resultMenuImage, null, 0, 0);
		int spacing = 50;
		//draw the winning players
		for(int i = 0; i < winners.size(); i++) {
			//error check
			if(winners.get(i) != null) {
				//get player id holding this color
				for(Player player : gameState.getPlayers()) {
					if(player.getPlayerColor().equals(winners.get(i))) {
						//match
						g.setColor(winners.get(i));
						g.setFont(new Font("Arial", Font.BOLD, 24));
						//bug fix
						if(i >= values.size()) {
//							g.drawString(values.size() + ". " + player.getPlayerID() + "     "+ String.valueOf(values.get(values.size()-1)), 105, 150+spacing*(values.size()-1));
							g.drawString(player.getPlayerID(), 105, 150+spacing*(values.size()-1));

						}else {
//							g.drawString((i+1) + ". " + player.getPlayerID() + "     "+ String.valueOf(values.get(i)), 105, 150+spacing*i);
							g.drawString(player.getPlayerID(), 105, 150+spacing*i);

						}
						break;
					}
				}

			}
		}
	}

	@Override
	public void tick() {
		//game ended start init
		if(!inited) {
			inited = true;
			//get the results
			results = gameBoard.getResults();
			//iterate keys
			for(Color key : results.keySet()) {
				//debug
				System.out.println("ResultMenu: Color in result set: " + key.toString());
				values.add(results.get(key));
			}
			//sort count array by highest value
			Collections.sort(values,Collections.reverseOrder());
			for(int i = 0; i < values.size(); i++) {
				//debug
				System.out.println("ResultMenu: ValueList: " + values.get(i));
			}


			//place the colors by highest value into winner list

			for(Color key : results.keySet()) {
				for(int i = 0; i < values.size(); i++) {
					//find this keys count position
					if(results.get(key) == values.get(i)) {
						//match found, place color into winner list at correct position
						System.out.println(i+". Added winner: " + key.toString());
						winners.add(i, key);
						break;
					}
				}
			}
			
		}
		
	}

}
