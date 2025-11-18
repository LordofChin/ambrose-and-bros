import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class MineSweeper {
	final double PERCENT_BOMBS = 0.1;
	
	char[][] solvedBoard;
	char[][] viewBoard;
	int numFlagged = 0;
	int numMines;
	int numMinesFound = 0;
	int size;
	
	int turnNum = 1;
	
	boolean gameOver = false;
	
	JFrame window;
	MineSweeperBoard gameWindow;
	
	
	MineSweeper(int size)
	{
		this.size = size;
		this.solvedBoard = new char[size][size];
		this.viewBoard = new char[size][size];
		for(char[] ca : viewBoard) {
			for(char c : ca) {
				c = ' ';
			}
		}
//		
		numMines = (int)Math.round((size*size) * PERCENT_BOMBS);
//		
		fillBoard(solvedBoard,numMines);
		
		this.window = new JFrame();
		
		gameWindow = new MineSweeperBoard(this, 7, numMines);
		gameWindow.updateView(viewBoard);
	}
	
	
	
	public void guess(int row, int collumn, boolean flag)
	{
		char guessChar = solvedBoard[row][collumn];
		if(flag) {
			if(guessChar == '*') {
				viewBoard[row][collumn] = 0;
				if(solvedBoard[row][collumn] == '!')
				{
					numMinesFound--;
				}
				numFlagged--;
			} else {
			viewBoard[row][collumn] = '*';
			if(solvedBoard[row][collumn] == '!')
			{
				numMinesFound++;
			}
			numFlagged++;
			}
			if(numMinesFound >= numMines)
			{
				System.out.println("You Win!");
				gameOver = true;
			}
		}else {
			if(guessChar == '!') {
				gameOver = true;
				this.viewBoard = this.solvedBoard;
				for(int i=0;i<size;i++) {
					for(int j=0;j<size;j++) {
						if(viewBoard[i][j] >= 'a') {
							viewBoard[i][j] = ' ';
						}
					}
				}
				
				System.out.println("Game Over");
			} else if(guessChar >= '1' && guessChar <= '9') {
				viewBoard[row][collumn] = guessChar;
			} else if(guessChar >= 'a') {
				guessSpread(guessChar,row,collumn);
//				int size = viewBoard.length;
//				for(int x=0;x<size;x++){
//					for(int y=0;y<size;y++){
//						
//						if(solvedBoard[x][y] == guessChar)
//						{
//							viewBoard[x][y] = '_';
//						}
//					}
//				}
			}
			turnNum++;
			
		}
		gameWindow.updateView(viewBoard);
	}
	
	/**
	 * Made for easier testing. Gives the two boards as options
	 * @param input - char: v or s
	 */
	public void printBoard(char input)
	{
		switch(input)
		{
		case 'v':
			displayBoard(viewBoard);
			break;
		case 's':
			displayBoard(solvedBoard);
			break;
		default:
			System.out.println("Error: input valid board");
			break;
		}
	}
	
	/**
	 * fills out a minesweeper board with all of the mines, numbers, and spaces
	 * @param newBoard - the board that will be filled
	 */
	private static void fillBoard(char[][] newBoard, int numMines) {
		Random rand = new Random();
		
		
		int size = newBoard[0].length;
		//int numSpaces = size * size;
		
		/*
		System.out.println(size);
		System.out.println(numSpaces);
		System.out.println(numMines);
		*/
		
		//Fills the board with mines
		for(int i = 0; i < numMines; i++) {
			boolean bool = false;
			
			//checks if spot is full before placing mine
			while(!bool) {
				int randRow = rand.nextInt(size);
				int randColumn = rand.nextInt(size);
			
				if(newBoard[randRow][randColumn] == '!') {
					bool = false;
				} else {
					newBoard[randRow][randColumn] = '!';
					bool = true;	
				}
			}
		}
		//fills the numbers
		for(int x=0;x<size;x++){
			for(int y=0;y<size;y++){
				if(newBoard[x][y] == 0)
				{
					int numAround = numAround(newBoard, x, y);
					if(numAround > 0)
					{
						newBoard[x][y] = Integer.toString(numAround).charAt(0);
					}
				}
			}
		}
		char replaceChar = 'a';
		for(int x=0;x<size;x++){
			for(int y=0;y<size;y++){
				if(newBoard[x][y] == 0) {
					spreadSpace(newBoard, replaceChar, x, y);
					replaceChar++;
				}
			}
		}
	}
	
	/**
	 * Displays the current state of a board
	 * @param viewBoard - the board that is displayed
	 */
	private static void displayBoard(char[][] viewBoard) {
		String board = "";
		for(int i = 0; i < viewBoard.length; i++) {
			for(int j = 0; j < viewBoard.length; j++) {
				board += "--";
			}
			board += "-\n";
			for(int k = 0; k < viewBoard.length; k++) {
				board += "|" + viewBoard[i][k];
			}
			board += "|\n";
		}
		for(int j = 0; j < viewBoard.length; j++) {
			board += "--";
		}
		board += "-\n";
		
		System.out.println(board);
	}
	
	
	/**
	 * Finds the amount of mines that are around a coordinate
	 * @param board
	 * @param x
	 * @param y
	 * @return - int amount of mines found
	 */
	private static int numAround(char[][] board, int x, int y)
	{
		int numMines = 0;
		int size = board[0].length;
		for(int i=-1;i<=1;i++){
			for(int j=-1;j<=1;j++){
				int guessX = x+i;
				int guessY = y+j;
				if((guessX >= 0 && guessX < size) && (guessY >= 0 && guessY < size))
				{
					if(board[guessX][guessY] == '!')
					{
						numMines++;
					}
				}
			}
		}
		return numMines;
	}
	/**
	 * recursively fills an empty space in a 2d char array
	 * @param board - the board that will be modified
	 * @param ch - the character that will replace the spaces
	 * @param row - part of the starting location for the function
	 * @param collumn - the other part of the starting location
	 */
	public static void spreadSpace(char[][] board, char ch, int row, int collumn)
	{
		board[row][collumn] = ch;
		
		if(collumn + 1 < board[0].length){
			if(board[row][collumn+1] == 0) {
				spreadSpace(board, ch, row, collumn + 1);
			}
		}
		if(row + 1 < board.length){
			if(board[row+1][collumn] == 0) {
				spreadSpace(board, ch, row + 1, collumn);
			}
		}
		if(collumn - 1 >= 0){
			if(board[row][collumn-1] == 0) {
				spreadSpace(board, ch, row, collumn - 1);
			}
		}
		if(row - 1 >= 0){
			if(board[row-1][collumn] == 0) {
				spreadSpace(board, ch, row - 1, collumn);
			}
		}
	}
	public void guessSpread(char guess, int row, int collumn)
	{
		viewBoard[row][collumn] = '_';
		int size = viewBoard.length;
		for(int i=-1;i<=1;i++){
			for(int j=-1;j<=1;j++){
				int guessX = row+i;
				int guessY = collumn+j;
				if((guessX >= 0 && guessX < size) && (guessY >= 0 && guessY < size))
				{
					char guessChar = solvedBoard[guessX][guessY];
					if(viewBoard[guessX][guessY] == 0)
					{
						if(solvedBoard[guessX][guessY] != '!')
						{
							if(guessChar >= '1' && guessChar <= '9') {
								viewBoard[guessX][guessY] = guessChar;
							} else if(guessChar == guess) {
								guessSpread(guess,guessX,guessY);
							}
						}
					}
				}
			}
		}
	}


}
class MineButton extends JButton {

	int[] coords = new int[2];
	static Color activeColor = new Color(189, 189, 189);
	
	public MineButton(int i, int j) {
		super();
		this.setBackground(activeColor);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.coords[0] = i;
		this.coords[1] = j;
	}
	
	public void setCoords(int i, int j) {
		this.coords[0] = i;
		this.coords[1] = j;
	}
	public int[] getCoords() {
		return coords;
	}
	
	
	
	public void setActive() {
		
	}
	
	public void setInactive() {
		this.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
		this.setEnabled(false);
	}
}
