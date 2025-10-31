package Game;
import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class RaceTrack extends JPanel {

    private final int rows = 10;
    private final int cols = 30;
    private final int START_COL = 1;
    private final int FINISH_COL = cols - 1;
    private final int[][] grid = new int[rows][cols]; // just for visuals
    
    //Synchronization for trackLock and winnerDeclaration
    private final Synchronization.CustomLock trackLock = new Synchronization.CustomLock();
    private final Synchronization.WinnerFlag winnerFlag = new Synchronization.WinnerFlag();
    

    private final List<Car> cars = new ArrayList<>();
    private final Color[] colors = {Color.RED, Color.BLUE, Color.ORANGE, Color.CYAN};

    public RaceTrack(int numCars) {
        // Mark start and finish lines
        for (int r = 0; r < rows; r++) {
            grid[r][START_COL] = 3;
            grid[r][FINISH_COL] = 4;
        }

        if (numCars <= 0) numCars = 1;
        for (int i = 0; i < numCars; i++) {
            int row = (numCars == 1) ? rows / 2 : (i * (rows - 1)) / (numCars - 1);
            Color color = colors[i % colors.length];
            cars.add(new Car(row, color));
        }
    }

    public boolean moveCar(Car c) {
    	trackLock.lock();
    	try {
            if (winnerFlag.getWinner() != null) return false;

            int nextCol = c.col + 1;

            if (nextCol >= FINISH_COL) {
                c.col = FINISH_COL;
                SwingUtilities.invokeLater(this::repaint);
                declareWinner(c);
                return false; // finished
            }

            // Simple collision prevention (prevents two cars from occupying the same space)
            for (Car other : cars) {
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
    public void declareWinner(Car c) {
        winnerFlag.setWinner(c);
    }

    public void resetCarsToStart() {
        trackLock.lock();
        try {
            for (Car c : cars) c.col = START_COL;
            winnerFlag.reset();
            repaint();
        } finally {
            trackLock.unlock();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
     // Acquire lock to read the current state of car positions
        trackLock.lock();
        try {

        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (c == START_COL) g.setColor(Color.GREEN);
                else if (c == FINISH_COL) g.setColor(Color.BLACK);
                else g.setColor(Color.WHITE);

                g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                g.setColor(Color.GRAY);
                g.drawRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
            }
        }

        for (Car c : cars) {
            g.setColor(c.color);
            g.fillRect(c.col * cellWidth, c.row * cellHeight, cellWidth, cellHeight);
        }
        
        // Draw Winner Text
        Car winner = winnerFlag.getWinner();
        if (winner != null) {
            g.setColor(winner.color); // Color would change depends on which car is the winner
            g.setFont(new Font("Inter", Font.BOLD, 30));
            String winText = "Winner: Car on Row " + (winner.row+1) + "!";
            FontMetrics fm = g.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(winText)) / 2;
            g.drawString(winText, textX, getHeight() - 10);
        }

        // START label
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.rotate(-Math.PI / 2, START_COL * cellWidth + 16, getHeight() / 2.0);
        g2.drawString("START", START_COL * cellWidth, getHeight() / 2);
        g2.dispose();

        // FINISH label
        g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.rotate(-Math.PI / 2, FINISH_COL * cellWidth + 16, getHeight() / 2.0);
        g2.drawString("FINISH", FINISH_COL * cellWidth + 5, getHeight() / 2);
        g2.dispose();
        
    } finally {
        trackLock.unlock();
    }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 450);
    }

    public List<Car> getCars() {
        return cars;
    }
}