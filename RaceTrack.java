import javax.swing.*;
import java.awt.*;

public class RaceTrack extends JPanel {

    private final int rows = 5;
    private final int cols = 30;
    private final int[][] grid = new int[rows][cols]; // 0 = empty, 1 = car1, 2 = car2, 3 = finish

    // Car positions
    private int car1Row = 1, car1Col = 0;
    private int car2Row = 3, car2Col = 0;

    public RaceTrack() {
        // Set finish line cells
        for (int r = 0; r < rows; r++) {
            grid[r][cols - 1] = 3; // finish line
        }
        // Place cars initially
        grid[car1Row][car1Col] = 1;
        grid[car2Row][car2Col] = 2;
    }

    public synchronized boolean moveCar(int carNumber) {
        int row = (carNumber == 1) ? car1Row : car2Row;
        int col = (carNumber == 1) ? car1Col : car2Col;

        if (col >= cols - 2) return false; // reached finish line

        int nextCol = col + 1;

        // Check if next cell is empty
        if (grid[row][nextCol] == 0) {
            // Move car
            grid[row][col] = 0;
            grid[row][nextCol] = carNumber;
            if (carNumber == 1) car1Col = nextCol;
            else car2Col = nextCol;

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

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int value = grid[r][c];
                if (value == 0) g.setColor(Color.WHITE);      // empty
                else if (value == 1) g.setColor(Color.RED);   // car1
                else if (value == 2) g.setColor(Color.BLUE);  // car2
                else if (value == 3) g.setColor(Color.BLACK); // finish

                g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);

                g.setColor(Color.GRAY); // grid lines
                g.drawRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
            }
        }
    }
}

