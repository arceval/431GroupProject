package Game;

import java.awt.Graphics2D;
import java.util.ArrayList;

import constaints.Drawable;
import controller.GameState;

public class GameBoard extends Drawable{
	//Individual Squares
	private ArrayList<Square> squareList = new ArrayList<Square>();
	//number of Rows
	private final int rows = 5;
	//number of Columns
	private final int columns = 5;
	//size of squares in pixels
	private int sizeOfSquares = 30;
	public GameBoard() {
		for(int y = 0; y < columns; y++) {
			for(int x = 0; x < rows; x++) {
				//add square to squareList
				squareList.add(new Square(GameState.numberOfPlayers, GameState.territoryLimit, GameState.penThickness, sizeOfSquares*x, sizeOfSquares*y, sizeOfSquares));
			}
		}
	}
	@Override
	public void render(Graphics2D g) {

		
	}

	@Override
	public void tick() {

		
	}

}
