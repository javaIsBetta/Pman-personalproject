import javax.swing.*;

public class GameWindow extends JFrame{
	GameWindow (){
		ArenaBuilder arena = new ArenaBuilder (25, 25, 4);
		GamePanel gp = new GamePanel(arena);
		add(gp);
		//title of the frame
		setTitle("Game");
		//if frame is closed, end program
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//panel where game logic is going to be created
		
		//add it to the frame
		
		//make contents of the frame scale to the size of the screen
		pack();
		setLocationRelativeTo(null); //this centers on the screen.
		//make frame visible
		setVisible(true);
	}
	
	public static void main(String [] args) {
        SwingUtilities.invokeLater(() -> { //lambda expression basically the same as writing:
        	//SwingUtilities.invokeLater(new Runnable() {
        	//@OVerride
        	//public void run(){
        	//new GameWindow(); }
            new GameWindow();
            });
        
	}
	
	

}
