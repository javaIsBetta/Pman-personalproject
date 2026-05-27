import javax.swing.*;

public class GameWindow extends JFrame{
	GameWindow (){
		//title of the frame
		setTitle("Game");
		//if frame is closed, end program
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//size of the frame
		setSize(70, 70);
		//panel where game logic is going to be created
		JPanel GamePanel = new JPanel ();
		//add it to the frame
		add(GamePanel);
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
