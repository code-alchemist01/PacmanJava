import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.HashSet;
import javax.swing.*;



public class Pacman extends JPanel  {
	
	private int rowSize = 21;
	private int columnSize = 19;
	private int tileSize = 32;
	private int boardWidth = columnSize * tileSize;
	private int boardHeight = rowSize * tileSize;
	
	private Image wallImage;
	private Image blueGhostImage;
	private Image redGhostImage;
	private Image pinkGhostImage;
	private Image orangeGhostImage;
	
	private Image pacmanUpImage;
	private Image pacmanDownImage;
	private Image pacmanLeftImage;
	private Image pacmanRightImage;
	
//	private Image scaredGhostImage;
//	
//	private Image cherryImage;
//	private Image cherry2Image;
//	private Image powerFoodImage;
	
	
	
	
	public Pacman() {
		setPreferredSize(new Dimension(boardWidth,boardHeight));
		setBackground(Color.black);
		
		// upload image
		
		// wall image
		wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
		
		// ghost image
		blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
		redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
		pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
		orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
		
		// pacman image
		pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
		pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
		pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
		pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
	}
	
	
	


}
