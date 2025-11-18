import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MineSweeperBoard extends JFrame implements ActionListener{

	JPanel buttonPanel;
	MineButton[][] buttonGrid;
	JPanel topPanel;
	JLabel minesRemaining;
	JButton flagButton;	
	static ImageIcon flagIcon = new ImageIcon("pickaxe.jpeg");
	
	MineSweeper linkedGame;
	
	int size;
	
	public MineSweeperBoard(MineSweeper game, int size, int numMines) throws HeadlessException {
		super();
		System.out.println(flagIcon.toString());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.linkedGame = game;
		this.size = size;
		this.setIconImage(flagIcon.getImage());
		this.buttonGrid = new MineButton[size][size];
		this.buttonPanel = new JPanel();
		this.buttonPanel.setLayout(new GridLayout(size,size));
		this.setLayout(new BorderLayout());
		this.flagButton = new JButton();
		this.flagButton.setIcon(flagIcon);
		this.flagButton.setPreferredSize(new Dimension(50,50));
		
		this.topPanel = new JPanel();
		this.minesRemaining = new JLabel(Integer.toString(numMines));
		this.topPanel.add(minesRemaining);
		this.topPanel.setPreferredSize(new Dimension(75,75));
		this.add(topPanel, BorderLayout.NORTH);
		
		this.topPanel.add(flagButton);
		
		
		
		
		
		for(int i = 0;i<size;i++) {
			for(int j = 0;j<size;j++) {
				MineButton button = new MineButton(i,j);
				button.setPreferredSize(new Dimension(50,50));
				button.addActionListener(this);
				this.buttonPanel.add(button);
				this.buttonGrid[i][j] = button;
			}
		}
		this.add(buttonPanel, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
	}
	
	public void updateView(char[][] board) 
	{
		for(int i = 0;i<size;i++) {
			for(int j = 0;j<size;j++) {
				this.buttonGrid[i][j].setText(String.valueOf(board[i][j]));
				System.out.println("Char: " + board[i][j]);
				if(board[i][j] == '_') {
					buttonGrid[i][j].setInactive();
				}
			}
		}
	}
	
	public void endGame() {
		for(MineButton[] a : buttonGrid) {
			for(MineButton b : a) {
				b.setEnabled(false);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof MineButton ) {
			MineButton button = (MineButton) e.getSource();
			int[] coords = button.getCoords();
			linkedGame.guess(coords[0], coords[1], false);
			
		}
	}
	
}
