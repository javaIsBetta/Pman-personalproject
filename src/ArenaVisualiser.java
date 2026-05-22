
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ArenaVisualiser {
    
    /**
     * Saves the arena grid as a PNG file
     * @param grid The arena grid (0 = floor, 1 = wall)
     * @param outputPath Where to save the PNG ("arena.png")
     * @param cellSize Size of each cell in pixels (5-10 is good)
     */
    public static void saveAsPNG(int[][] grid, String outputPath, int cellSize) {
        int height = grid.length;
        int width = grid[0].length;
        
        // Create an image in memory
        BufferedImage image = new BufferedImage(
            width * cellSize, 
            height * cellSize, 
            BufferedImage.TYPE_INT_RGB
        );
        
        // Get the graphics object to draw with
        Graphics2D g = image.createGraphics();
        
        // Loop through every cell in the grid
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // Choose color based on cell value
                Color cellColor;
                if (grid[row][col] == 1) {
                    cellColor = Color.BLACK;  // Walls
                } else if (grid[row][col] == 0){
                    cellColor = Color.WHITE;  // Floors
                }
                else { //user spawn point
                	cellColor = Color.GREEN;
                }
                
                // Draw the cell as a filled rectangle
                g.setColor(cellColor);
                g.fillRect(
                    col * cellSize, 
                    row * cellSize, 
                    cellSize, 
                    cellSize
                );
            }
        }
        
        // Optional: Draw grid lines (helps see individual cells)
        g.setColor(Color.GRAY);
        for (int row = 0; row <= height; row++) {
            g.drawLine(0, row * cellSize, width * cellSize, row * cellSize);
        }
        for (int col = 0; col <= width; col++) {
            g.drawLine(col * cellSize, 0, col * cellSize, height * cellSize);
        }
        
        // Clean up graphics object
        g.dispose();
        
        // Save the image to file
        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "png", outputFile);
            System.out.println("Arena saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving arena image: " + e.getMessage());
        }
    }
    
    /**
     * Alternative version with customizable colors
     */
    public static void saveAsPNG(int[][] grid, String outputPath, int cellSize, 
                                   Color floorColor, Color wallColor) {
        int height = grid.length;
        int width = grid[0].length;
        
        BufferedImage image = new BufferedImage(
            width * cellSize, 
            height * cellSize, 
            BufferedImage.TYPE_INT_RGB
        );
        
        Graphics2D g = image.createGraphics();
        
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color cellColor = (grid[row][col] == 1) ? wallColor : floorColor;
                g.setColor(cellColor);
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
        
        g.dispose();
        
        try {
            ImageIO.write(image, "png", new File(outputPath));
            System.out.println("Arena saved to: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error saving arena image: " + e.getMessage());
        }
    }
}
