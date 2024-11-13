import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		int rowSize = 21;
		int columnSize = 19;
		int tileSize = 32;
		int boardWidth = columnSize * tileSize;
		int boardHeight = rowSize * tileSize;
		
		
		JFrame frame = new JFrame("PacMan");
		frame.setSize(boardWidth, boardHeight);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Pacman pacman = new Pacman();
		frame.add(pacman);
		frame.pack();
		frame.setVisible(true);
		

	}

}
