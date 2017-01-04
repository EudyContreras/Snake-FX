package com.EudyContreras.Snake.Utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        private int startAt = 0;
        private int endAt;
        private int x = startAt;
        private Timer timer;
        private Interpolator splineInterpolator;
        private long startTime = -1;
        private long playTime = 5000; // 5 seconds

        public TestPane() {
            splineInterpolator = Interpolators.getEasingInstance(0.5f,0.5f);
            timer = new Timer(5, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (startTime < 0) {
                        startTime = System.currentTimeMillis();
                    }
                    long now = System.currentTimeMillis();
                    long duration = now - startTime;
                    float t = (float) duration / (float) playTime;
                    if (duration >= playTime) {
                        t = 1;
                    }

                    double progress = splineInterpolator.interpolate(t);

                    x = startAt + ((int) Math.round((endAt - startAt) * progress));
                    repaint();
                }
            });
            timer.setInitialDelay(0);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!timer.isRunning()) {
                        startTime = -1;
                        startAt = 0;
                        endAt = getWidth() - 10;
                        timer.start();
                    }
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.RED);
            g2d.fillRect(x, (getHeight() / 2) - 5, 10, 10);
            g2d.dispose();
        }

    }

}