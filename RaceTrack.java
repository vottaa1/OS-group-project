import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class RaceTrack extends JPanel {

    private final int rows = 10;
    private final int cols = 30;
    private final int startCol = 1;
    private final int finishCol = cols-1;
    private final int[][] grid = new int[rows][cols]; // 0 = empty, 1 = car1, 2 = car2, 3 = finish

    private final List<car> cars =new ArrayList<>();
    private final Color[] colors = {Color.RED, Color.BLUE, Color.ORANGE, Color.CYAN};

    public RaceTrack(int numCars) {
        // Set finish line cells
        for (int r = 0; r < rows; r++) {
            grid[r][finishCol] = 4; // finish line
        }
        
        // set starting line 
        for (int r=0; r<rows; r++) {
        	grid[r][1]=3;
        }
        //spacing for cars varies based on how many cars 
        int avalibleRows = rows;
        for (int i = 0; i<numCars; i++) {
        	int row = i* (avalibleRows -1)/ (numCars-1);
        	Color color= colors[i%colors.length];
        	cars.add(new car (row, color));
        }
       
    }
        

    public synchronized boolean moveCar(car car) {
        int row = car.row;
        int col = car.col;
        int nextCol = col+1;
      

        if (nextCol >= cols - 2) return false; // reached finish line
        nextCol= car.col+1;
        
        

        // Check if next cell is empty
        if (grid[car.row][nextCol] == 0 || grid[row][nextCol] == 3 ) { //empty space infront of car (besides start line)
            // Move car
            car.col = nextCol;
            
            repaint(); // redraw the panel
            return true;
        }
        return false; // cell occupied, wait
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;
        int startCol =1; //col for starting line 
        int finishCol= cols-1; //col for finish line

        //drawing track
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                
                // draw starting/finish lines 
                if (c == startCol) g.setColor(Color.GREEN); //start line
                else if (c == finishCol) g.setColor(Color.BLACK);      // finish line
                else  g.setColor(Color.WHITE); // finish
                g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                
                g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);

                g.setColor(Color.GRAY); // grid lines
                g.drawRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                
                
                }
       
            }
            //draw cars 
            for (car car:cars) {
            	g.setColor(car.color);
            	g.fillRect(car.col * cellWidth, car.row * cellHeight, cellWidth, cellHeight);
        }

     // Draw START text vertically
        Graphics2D g2 = (Graphics2D) g.create(); // create a copy to avoid rotating everything
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        // Rotate 90° counter-clockwise around the point where we want the text
        g2.rotate(-Math.PI / 2, startCol * cellWidth +16, getHeight() / 2);
        g2.drawString("START", startCol * cellWidth, getHeight() / 2);
        g2.dispose(); // restore original graphics

        // Draw FINISH text vertically
        g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.rotate(-Math.PI / 2, finishCol * cellWidth +16, getHeight() / 2);
        g2.drawString("FINISH", finishCol * cellWidth + 5, getHeight() / 2);
        g2.dispose();
    
    }
    
    public List<car> getCars(){
    	return cars;
    }
    
    
}

