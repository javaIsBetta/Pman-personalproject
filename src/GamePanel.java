import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener{
	ArenaBuilder ab;
	int [][] grid;
	int plyrX;
	int plyrY;
	int cellSize;
	int gridWidth; //for cell Size calculation
	int gridHeight;
	Timer t;
	
	GamePanel (ArenaBuilder a){
		this.ab = a;
		grid = ab.getGrid();
		plyrX = ab.getSpawnPoint()[0];
		plyrY = ab.getSpawnPoint()[1];
		System.out.println("Spawn point: " + plyrX + ", " + plyrY);
		gridWidth = grid[0].length;
		gridHeight = grid.length;
		setFocusable(true);
		//this.setSize(25,25);
		addKeyListener(this);
		Timer gameTimer = new Timer (16, new ActionListener() { //16 ms btwn ticks -> 60FPS
			@Override
			public void actionPerformed(ActionEvent e) {
				//Allowed inputs: arrows, and WASD.
				repaint();
			}
		});
		gameTimer.start();
		
		
		//A = go left -- -1 x
		//W = go up -- +1 y
		//S = go down -- -1 y
		//D = go right -- +1 x
		

	}
	
	@Override 
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		//cellSize = this.getWidth()/gridWidth;
		cellSize =25;
		Color c = null;
		for (int row =0; row< gridHeight; row++) {
			for (int col =0; col<gridWidth; col++) {
				if(grid[row][col] == 1) {
					c = Color.black;
				}
				else if(grid[row][col] == 0) {
					c = Color.white;
				}
				g.setColor(c);
				g.fillRect(row*cellSize, col*cellSize, cellSize, cellSize);
			}
			
			
		}
		for (int row =0; row< gridHeight; row++) {
			for (int col =0; col<gridWidth; col++) {
				g.setColor(Color.GRAY);
				g.drawRect(row*cellSize, col*cellSize, cellSize, cellSize);
			}
		}
        
		c= Color.green;
		g.setColor(c);
		g.fillRect(plyrX*cellSize, plyrY*cellSize, cellSize, cellSize);
		
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case 38: //Up arrow
				tryMove(plyrX,plyrY-1); //inverse for some reason? REVIST
				System.out.println("Player moved up!");
				break;
			case 40: //Down arrow
				tryMove(plyrX,plyrY+1); //inverse for some reason?
				System.out.println("Player moved down!");
				break;
			case 39: // Rigth arrow
				tryMove(plyrX+1,plyrY);
				System.out.println("Player moved right!");
				break;
			case 37: // Left arrow
				tryMove(plyrX-1,plyrY);
				System.out.println("Player moved left!");
				break;
				
		}
				
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
		
	}
	
	private void tryMove(int x, int y) {
		if (grid[x][y] == 1) {
			System.out.println("Player hit a wall!");
			//cannot move there //nothing happens maybe bounce back animation? //error sound?
		}
		else { // 0 or or 2
			plyrX = x;
			plyrY =y;
			repaint();
		}
	}
	
	

}
