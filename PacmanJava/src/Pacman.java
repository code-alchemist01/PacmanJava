import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.HashSet;
import javax.swing.*;

public class Pacman extends JPanel implements ActionListener, KeyListener {

	class Block {
		int x;
		int y;
		int width;
		int height;
		Image image;

		int startX;
		int startY;

		char direction = 'U';
		int speedX = 0;
		int speedY = 0;

		public Block(Image image, int x, int y, int width, int height) {
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.startX = x;
			this.startY = y;
		}

		void updateDirection(char direction) {

			char prevDirection = this.direction;
			this.direction = direction;
			updateSpeed();
			this.x += this.speedX;
			this.y += this.speedY;

			for (Block wall : walls) {
				if (collision(this, wall)) {
					this.x -= this.speedX;
					this.y -= this.speedY;
					this.direction = prevDirection;
					updateSpeed();
				}
			}

		}

		void updateSpeed() {

			if (this.direction == 'U') {
				this.speedX = 0;
				this.speedY = -tileSize / 4;
			} else if (this.direction == 'D') {
				this.speedX = 0;
				this.speedY = tileSize / 4;
			} else if (this.direction == 'L') {
				this.speedX = -tileSize / 4;
				this.speedY = 0;
			} else if (this.direction == 'R') {
				this.speedX = tileSize / 4;
				this.speedY = 0;
			}

		}
		void reset() {
			this.x = this.startX;
			this.y = this.startY;
		}
	}

	private int rowSize = 21;
	private int columnSize = 19;
	private int tileSize = 32;
	private int boardWidth = columnSize * tileSize;
	private int boardHeight = rowSize * tileSize;

	private Image wallImage;
	private Image blueGhostImage;
	

	private Image pacmanUpImage;
	private Image pacmanDownImage;
	private Image pacmanLeftImage;
	private Image pacmanRightImage;

//	private Image scaredGhostImage;
//	
//	private Image cherryImage;
//	private Image cherry2Image;
//	private Image powerFoodImage;

	// X = wall, O = skip, P = pac man, ' ' = food
	// Ghosts: b = blue, o = orange, p = pink, r = red

	HashSet<Block> walls;
	HashSet<Block> foods;
	HashSet<Block> ghosts;
	Block pacman;

	Timer loop;
	char[] directions = { 'U', 'D', 'L', 'R' };
	Random random = new Random();

	int score = 0;
	int heart = 3;
	boolean over = false;

	public Pacman() {
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setBackground(Color.black);
		addKeyListener(this);
		setFocusable(true);

		// upload image

		// wall image
		wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();

		// ghost image
		blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
		

		// pacman image
		pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
		pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
		pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
		pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

		loadMap();

		for (Block ghost : ghosts) {
			char newDirection = directions[random.nextInt(4)];
			ghost.updateDirection(newDirection);
		}

		loop = new Timer(50, this);
		loop.start();

	}

	public void loadMap() {
		walls = new HashSet<Block>();
		foods = new HashSet<Block>();
		ghosts = new HashSet<Block>();

		generateMap();
	}

	private void generateMap() {
		Random random = new Random();
		for (int r = 0; r < rowSize; r++) {
			for (int c = 0; c < columnSize; c++) {
				// Duvarları rastgele yerleştir
				if (random.nextDouble() < 0.2) { // %20 ihtimalle duvar
					Block wall = new Block(wallImage, c * tileSize, r * tileSize, tileSize, tileSize);
					walls.add(wall);
				} else if (random.nextDouble() < 0.1) { // %10 ihtimalle yiyecek
					Block food = new Block(null, c * tileSize + 10, r * tileSize + 10, 4, 4);
					foods.add(food);
				}
			}
		}

		// Pacman ve hayaletleri yerleştir
		pacman = new Block(pacmanRightImage, tileSize, tileSize, tileSize, tileSize); // Başlangıç konumu
		Block ghost = new Block(blueGhostImage, tileSize * 5, tileSize * 5, tileSize, tileSize); // Başlangıç konumu
		ghosts.add(ghost);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

		for (Block ghost : ghosts) {
			g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
		}

		for (Block wall : walls) {
			g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
		}

		g.setColor(Color.yellow);
		for (Block food : foods) {
			g.fillRect(food.x, food.y, food.width, food.height);
		}
		g.setFont(new Font("Arial", Font.BOLD, 20));
		if (over) {
			g.drawString("Oyun Bitti... " + String.valueOf(score), tileSize / 2, tileSize / 2);
		} else {
			g.drawString("x" + String.valueOf(heart) + " Score: " + String.valueOf(score), tileSize / 2, tileSize / 2);
		}

	}

	public void move() {
		pacman.x += pacman.speedX;
		pacman.y += pacman.speedY;

		for (Block wall : walls) {
			if (collision(pacman, wall)) {
				pacman.x -= pacman.speedX;
				pacman.y -= pacman.speedY;
				break;
			}
		}
		for (Block ghost : ghosts) {
			if(collision(ghost, pacman)) {
				heart -= 1;
				if(heart == 0) {
					over = true;
					return;
				}
				resetPositions();
			}
			if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
				ghost.updateDirection('U');
			}
			ghost.x += ghost.speedX;
			ghost.y += ghost.speedY;
			for (Block wall : walls) {
				if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
					ghost.x -= ghost.speedX;
					ghost.y -= ghost.speedY;
					char newDirection = directions[random.nextInt(4)];
					ghost.updateDirection(newDirection);
				}
			}
		}

		Block foodEaten = null;
		for (Block food : foods) {
			if (collision(pacman, food)) {
				foodEaten = food;
				score += 10;
			}
		}
		foods.remove(foodEaten);
	}
	
	public void resetPositions() {
		pacman.reset();
		pacman.speedX = 0;
		pacman.speedY = 0;
		for(Block ghost : ghosts) {
			ghost.reset();
			char newDirection = directions[random.nextInt(4)];
			ghost.updateDirection(newDirection);
		}
	}
	
	

	public boolean collision(Block a, Block b) {
		return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		move();
		repaint();
		if(over) {
			loop.stop();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if(over) {
			loadMap();
			resetPositions();
			heart = 3;
			score = 0;
			over = false;
			loop.start();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			pacman.updateDirection('U');
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			pacman.updateDirection('D');
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			pacman.updateDirection('L');
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			pacman.updateDirection('R');
		}

		if (pacman.direction == 'U') {
			pacman.image = pacmanUpImage;
		} else if (pacman.direction == 'D') {
			pacman.image = pacmanDownImage;
		} else if (pacman.direction == 'L') {
			pacman.image = pacmanLeftImage;
		} else if (pacman.direction == 'R') {
			pacman.image = pacmanRightImage;
		}

	}

}
