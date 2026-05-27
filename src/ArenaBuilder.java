import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
class ArenaBuilder {
	private static int[][] grid;
	private static int [][] gridBuffer; //for cellular automation processing
	int xlength;
	int ylength;
	private int spawnX;
	private int spawnY;
	private int seed;
	 private static Random rnd;
	 private int [] values = {0, 1}; //0 for open space, 1 for wall
	private  double [] weights = {0.55, 0.45}; //chance of the coordinate at the grid being either open
	private double[] cumulativeWeights;
	 //space or a wall that you cannot go through
	 //currently with this implementation there is a chance the user may spawn in an open space and all
	 //surrounding directions be walls (odds are low but it is possible), essentially trapping them, this will be resolved later on
	public ArenaBuilder(int x, int y, int iter) { // completely random implementation.
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
		genSpawnPoint();
		closeOffPockets();
		
		}
	
	public ArenaBuilder(int x, int y, int s, int iter) { // has a seed parameter that allows deterministic generation
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
		rnd = new Random(s);
		this.cumulativeWeights = new double [weights.length];
		this.cumulativeWeights[0] = weights[0];
		for (int i = 1; i<weights.length; i++) {
			this.cumulativeWeights[i] = this.cumulativeWeights[i-1] + weights[i];
		}
		//populate within the borders
		populateGrid();
		cellularAutomata(iter);
		genSpawnPoint();
		closeOffPockets();
		}
	public int[][] getGrid (){
		return grid;
	}
	public int[] getSpawnPoint () {
		int [] SP= {spawnX, spawnY};
		return SP;
	}
	
    private int getRandomValue() {
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
	
	private void genSpawnPoint () {
		int x;
		int y;
		int count = 10;
		while (true) {
			 x = ThreadLocalRandom.current().nextInt(1, xlength-1);
			 y = ThreadLocalRandom.current().nextInt(1, ylength-1);
			 if (grid[x][y] == 1) {
				 count--;
				 if (count ==0) {
					 System.out.println("Could not find a spawn point");
					 System.exit(1);
				 }
				 //only repeat this for count tries, if spawn point not found in ten attempts, grid surely must not have a lot of space for user
				 //or we jut got unlucky and only got walls.
				 //go again
				 //this is probably not optimal, just trying somethin
			 }
			 else {
				 grid[x][y] = 2;
				 spawnX =x;
				 spawnY = y;
				 break;
			 }
		}
		
	}
	
	
	private void closeOffPockets() { //flood fill
		boolean [][] flood = new boolean [xlength][ylength];
		Queue<int[]> BFS = new LinkedList<>();
		//start from the spawn point
		//apply bfs from there on marking cells visitied in the boolean grid, so we dont visit them again
		//methodology: add spwan point to the queue, mark visited, add the adjacent elements (theyre still marked unvisited)
		//
		BFS.add(new int[] {spawnX, spawnY});
		flood[spawnX][spawnY] = true;
		int tempX ;
		int tempY ;
		while (!BFS.isEmpty()) {
			//add adjacent elements to the queue, move onto next one and check adjacent elements, repeat pattern until queue is empty
			//only directions: up, down, left, right
			//(x,y), (x-1,y)= left, (x+1,y) = right, (x, y-1) = down, (x,y+1) = up
			//do for each cardinal direction
			int[] current = BFS.poll(); // x and y values in the returned array
			tempX = current[0];
			tempY = current[1];
			//four if statements for each cardinal neighbor
			if (grid[tempX-1][tempY] == 0 && flood[tempX-1][tempY] == false) {
				BFS.add(new int[] {tempX-1, tempY});
				flood[tempX-1][tempY] = true; // set visited to true
				//BFS.poll(); //rempve from the queue
			}
			if (grid[tempX+1][tempY] == 0 && flood[tempX+1][tempY] == false) {
				BFS.add(new int[] {tempX+1, tempY});
				flood[tempX+1][tempY] = true;
				//BFS.poll();
			}
			if (grid[tempX][tempY-1] == 0 && flood[tempX][tempY-1] == false) {
				BFS.add(new int[] {tempX, tempY-1});
				flood[tempX][tempY-1] = true;
				//BFS.poll();
			}
			if (grid[tempX][tempY+1] == 0 && flood[tempX][tempY+1] == false) {
				BFS.add(new int[] {tempX, tempY+1});
				flood[tempX][tempY+1] = true;
				//BFS.poll();
			}
			//
		}
		//We've identified the reachable pocket, close off the unreachable one.
		for (int i =1; i<xlength-1; i++) {
			for (int j = 1; j<ylength-1; j++) {
				if (grid[i][j] ==0 && flood[i][j] == false) {
					grid[i][j] = 1; //not reached, so turn it into a wall.
				}
			}
		}
	}
	
	
	/*public static void main(String [] args) {
		//ArenaBuilder ab = new ArenaBuilder(25,25,5);
		ArenaBuilder ab = new ArenaBuilder(25, 25, 17, 5);// for reproducable arena -- note spawn point will most likely be different
		ArenaVisualiser.saveAsPNG(grid, "/Users/konkevezi/Desktop/arena.png", 30);

	}*/
	
	
}
