import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class RaceTrack extends JPanel {

	//initalize variables to draw grid
    private final int rows = 10;
    private final int cols = 30;
    private final int startCol = 1;
    private final int finishCol = cols-1;
    private final int[][] grid = new int[rows][cols]; 

    private volatile boolean raceOver = false;
    private car winner = null;
    
    
    //initalize list of cars 
    private final List<car> cars =new ArrayList<>();
    private final Color[] colors = {Color.RED, Color.BLUE, Color.ORANGE, Color.CYAN};
    
    //Synchornization locks for mutex locks
    private final Synchronization.CustomLock trackLock = new Synchronization.CustomLock();
    private final Synchronization.WinnerFlag winnerFlag = new Synchronization.WinnerFlag();

    //create race track
    public RaceTrack(int numCars) {
        // Set finish line col
        for (int r = 0; r < rows; r++) {
            grid[r][finishCol] = 4; // finish line
        }
        
        // set starting line 
        for (int r=0; r<rows; r++) {
        	grid[r][1]=3;
        }
        
        //add cars to track 
        int avalibleRows = rows;
        for (int i = 0; i<numCars; i++) {
        	//evenly spaces cars out based on how many cars there are. 
        	int row = i* (avalibleRows -1)/ (numCars-1); 
        	Color color= colors[i%colors.length]; // cars colors alternate
        	cars.add(new car (row, color)); //add new car to track
        }
       
    }
    
    public boolean moveCar(car c) {
    	trackLock.lock();
    	try {
            if (winnerFlag.getWinner() != null) return false;

            int nextCol = c.col + 1;

            if (nextCol >= finishCol) {
                c.col = finishCol;
                SwingUtilities.invokeLater(this::repaint);
                declareWinner(c);
                return false; // finished
            }

            // Simple collision prevention (prevents two cars from occupying the same space)
            for (car other : cars) {
                if (other != c && other.row == c.row && other.col == nextCol) {
                    // Car cannot move forward, stays in place this turn
                    return true; 
                }
            }

            c.col = nextCol;
            SwingUtilities.invokeLater(this::repaint);
            return true;
        } finally {
            trackLock.unlock();
        }
    }

    public boolean isRaceOver() {
    	return winnerFlag.getWinner() != null;
    }

    //Synchronization for the finish line
    //Make sure that no car can enter if there is a winner
    public void declareWinner(car c) {
        winnerFlag.setWinner(c);
    }

    public void resetCarsToStart() {
        trackLock.lock();
        try {
            for (car c : cars) c.col = startCol;
            winnerFlag.reset();
            repaint();
        } finally {
            trackLock.unlock();
        }
    }


       
 //draw graphics 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;
        int startCol =1; //col for starting line 
        int finishCol= cols-1; //col for finish line

//drawing track
        //nested for loop to make grid 
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                
                // draw starting/finish lines 
                if (c == startCol) g.setColor(Color.GREEN); //start line
                else if (c == finishCol) g.setColor(Color.BLACK);      // finish line
                else  g.setColor(Color.WHITE); // empty space on track
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
            
            // Draw Winner Text
            car winner = winnerFlag.getWinner();
            if (winner != null) {
                g.setColor(winner.color); // Color would change depends on which car is the winner
                g.setFont(new Font("Inter", Font.BOLD, 30));
                String winText = "Winner: Car on Row " + (winner.row+1) + "!";
                FontMetrics fm = g.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(winText)) / 2;
                g.drawString(winText, textX, getHeight() - 10);
            }  

     // Draw START text vertically
        Graphics2D g2 = (Graphics2D) g.create(); // create a copy to avoid rotating everything
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        // Rotate text 90° counter-clockwise 
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
    
    @Override
    public Dimension getPreferredSize() {
    	return new Dimension(900, 450);
    }
    
    //return method for main method
    public List<car> getCars(){
    	return cars;
    }  
    
    public int getFinishCol() {
    	return finishCol;
    }
    
}

