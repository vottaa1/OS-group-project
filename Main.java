import javax.swing.*;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Grid-Based Racing Game");
            int numCars = 5;
            final RaceTrack track = new RaceTrack(numCars);

            frame.setLayout(new BorderLayout());
            frame.add(track, BorderLayout.CENTER);

            // Control Panel
            JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton startBtn = new JButton("Start");
            JButton pauseBtn = new JButton("Pause");
            JButton resumeBtn = new JButton("Resume");
            JButton stopBtn = new JButton("Stop");
            JButton resetBtn = new JButton("Reset");
            JButton restartBtn = new JButton("Restart");

            controls.add(startBtn);
            controls.add(pauseBtn);
            controls.add(resumeBtn);
            controls.add(stopBtn);
            controls.add(resetBtn);
            controls.add(restartBtn);
            frame.add(controls, BorderLayout.NORTH);

            // Controller
            RaceController controller = new RaceController(
                    track, startBtn, pauseBtn, resumeBtn, stopBtn, resetBtn, restartBtn
            );

            startBtn.addActionListener(e -> controller.startRace());
            pauseBtn.addActionListener(e -> controller.pauseRace());
            resumeBtn.addActionListener(e -> controller.resumeRace());
            stopBtn.addActionListener(e -> controller.stopRace());
            resetBtn.addActionListener(e -> controller.resetRace());
            restartBtn.addActionListener(e -> controller.restartRace());

            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}