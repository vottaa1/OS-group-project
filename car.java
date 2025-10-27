import javax.swing.*;

public class car {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Grid-Based Racing Game");
                final RaceTrack track = new RaceTrack();

                frame.add(track);
                frame.setSize(800, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // Create and start car threads
                Thread car1Thread = new Thread(new Runnable() {
                    public void run() {
                        moveCar(track, 1);
                    }
                });

                Thread car2Thread = new Thread(new Runnable() {
                    public void run() {
                        moveCar(track, 2);
                    }
                });
                car1Thread.start();
                car2Thread.start();
            }
        });
    }

    private static void moveCar(RaceTrack track, int carNumber) {
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
    }
}
