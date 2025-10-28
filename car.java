import javax.swing.*;
import java.awt.*;

public class car {
	int row;
	int col;
	Color color;
	
	public car(int row, Color color) {
		this.row=row;
		this.col=0;
		this.color=color;
	}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Grid-Based Racing Game");
                int numCars= 5;
                final RaceTrack track = new RaceTrack(5);

                frame.add(track);
                frame.setSize(800, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // Create and start car threads
                for (car car : track.getCars()) { // assuming you add a getter in RaceTrack
                    new Thread(() -> {
                        try {
                            boolean moving = true;
                            while (moving) {
                                Thread.sleep((int) (Math.random() * 500 + 200)); // random speed
                                synchronized (track) {
                                    moving = track.moveCar(car);
                                }
                            }
                            System.out.println("Car finished on row " + car.row);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        });


  /*  private static void moveCar(RaceTrack track, int carNumber) {
        try {
            boolean moving = true;
            while (moving) {
                Thread.sleep((int)(Math.random() * 500 + 200)); // random speed
                synchronized (track) {
                    moving = track.moveCar(carNumber);
                }
            }
            System.out.println("Car " + carNumber + " finished!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
        }
}
