package Game;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import constraints.Drawable;
import controller.GameState;

public class GameBoard extends Drawable implements MouseListener{
	//Individual Squares
	private ArrayList<Square> squareList = new ArrayList<Square>();
	//number of Rows
	private final int rows = 5;
	//number of Columns
	private final int columns = 5;
	//size of squares in pixels
	private int sizeOfSquares = 30;
	public GameBoard() {
		int squareID = 0;
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < columns; y++) {
				//add square to squareList
				squareList.add(new Square(GameState.numberOfPlayers, squareID, GameState.territoryLimit, GameState.penThickness, sizeOfSquares*x, sizeOfSquares*y, sizeOfSquares));
				squareID++;
			}
		
		}
	}
	@Override
	public void render(Graphics2D g) {
		//for each square in the gameboard
		for(Square square : squareList) {
			//render the square
			square.render(g);
		}
		
	}

	@Override
	public void tick() {

		
	}
	//Mouse click happened
	@Override
	public void mouseClicked(MouseEvent e) {
		//For each square in the gameboard
		for(Square square : squareList) {
			//check if it was the one clicked
			if(square.isClicked(e)) {
				//The square has been clicked
				//get the square id and gamestate->connectionEndpoint->server to let them know of this update
				square.getSquareID();
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
