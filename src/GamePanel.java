import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel{
	int [][] grid;
	int spawnX;
	int spawnY;
	Timer t;
	
	GamePanel (int [][]g, int []sp){
		grid = g;
		spawnX = sp[0];
		spawnY = sp[1];
		Timer gameTimer = new Timer (16, new ActionListener() { //16 ms btwn ticks -> 60FPS
			@Override
			public void actionPerformed(ActionEvent e) {
				//Allowed inputs: arrows, and WASD.
				repaint();
			}
		});
		

	}

}
