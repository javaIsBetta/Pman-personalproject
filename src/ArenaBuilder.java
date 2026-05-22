import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;
class ArenaBuilder {
	static int[][] grid;
	static int [][] gridBuffer; //for cellular automation processing
	int xlength;
	int ylength;
	 private static Random rnd;
	 private int [] values = {0, 1}; //0 for open space, 1 for wall
	private  double [] weights = {0.55, 0.45}; //chance of the coordinate at the grid being either open
	private double[] cumulativeWeights;
	 //space or a wall that you cannot go through
	 //currently with this implementation there is a chance the user may spawn in an open space and all
	 //surrounding directions be walls (odds are low but it is possible), essentially trapping them, this will be resolved later on
	public ArenaBuilder(int x, int y, int iter) {
		this.xlength = x+2;//2 rows added for borders
		this.ylength = y+2;//2 columns added for borders
		grid = new int[xlength][ylength];
		gridBuffer = new int [xlength][ylength];
		//set every element to 1 in the grid;
		for (int i = 0; i<xlength; i++) {
			for (int j =0; j<ylength; j++) {
				grid[i][j] = 1;
				gridBuffer[i][j] = 1;
			}
		}
		rnd = new Random();
		this.cumulativeWeights = new double [weights.length];
		this.cumulativeWeights[0] = weights[0];
		for (int i = 1; i<weights.length; i++) {
			this.cumulativeWeights[i] = this.cumulativeWeights[i-1] + weights[i];
		}
		//populate within the borders
		populateGrid();
		cellularAutomata(iter);
		}
	
    public int getRandomValue() {
        double rand = rnd.nextDouble() * cumulativeWeights[cumulativeWeights.length - 1]; //random number will be in between 0 to 100
        
        // Binary search for O(log n) performance
        int index = Arrays.binarySearch(cumulativeWeights, rand);
        if (index < 0) {
            index = -index - 1;
        }
        return values[index];
    }
    

	private void populateGrid() { //offset by 1 for borders
		 for (int i = 1; i < xlength-1; i++) {
	            for (int j = 1; j < ylength-1; j++) {
	                grid[i][j] = getRandomValue();
	            }
	        }
	       
		
	}
	
	
	private void cellularAutomata (int iterations){
		/*New rule for a more structured arena: birth/survival rule:
		A tile becomes a wall if it was a wall and 4 or more of the 8 adjacent elements were
		walls, or if it was not a wall and 5 or moe neighbors were-- so simply --
		Let x be the current position we are on.
		if (x == 1 && countOfWalls>=4) || (x!=1 && countOfWalls>=5) { x =1;
		in any other case it is an open tile (0)
		*/
		int newValue;
		for (int k =0; k<iterations; k++) {
			//int newGrid [][] = new int [xlength][ylength];
		for (int i = 1; i<xlength-1 ; i++ ) {
			for (int j = 1; j <ylength-1; j++) {
				newValue = grid[i-1][j] +grid[i+1][j] +
						grid[i-1][j-1] +grid[i+1][j+1] + 
						grid[i-1][j+1] +grid[i+1][j-1] + 
						grid[i][j-1] +grid[i][j+1] ;
				//newValue still works as the count of surrounding walls
				if ((grid[i][j] == 1 && newValue>=4) || (grid[i][j] == 0 && newValue >= 5)) {		
				gridBuffer[i][j] = 1;
			}
				else {
					gridBuffer[i][j]=0;
				}
		}
	}
		int[][] temp = grid;
		grid = gridBuffer;
		gridBuffer = temp;
		//printGrid();
		//System.out.println();
		//System.out.println();
		}
	}
	
	public void printGrid () { 
		 for (int i = 0; i < xlength; i++) {
	            for (int j = 0; j < ylength; j++) {
	                System.out.print(grid[i][j]);
	            }
	            System.out.println("\n");
	        }
	}
	
	public void genSpawnPoint () {
		int x;
		int y;
		while (true) {
			 x = ThreadLocalRandom.current().nextInt(2, xlength-2);
			 y = ThreadLocalRandom.current().nextInt(2, ylength-2);
			 if (grid[x][y] == 1) {
				 //go again
				 //this is probably not optimal, just trying somethin
			 }
			 else {
				 grid[x][y] = 2;
				 break;
			 }
		}
		
	}
	
	public static void main(String [] args) {
		ArenaBuilder ab = new ArenaBuilder(25,25,4);
		ab.genSpawnPoint();
		ArenaVisualiser.saveAsPNG(grid, "/Users/konkevezi/Desktop/arena.png", 7);

	}
	
	
}
