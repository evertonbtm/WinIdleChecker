package br.winstate.service.state;

import br.winstate.service.message.MessageService;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.concurrent.ExecutionException;

public class WinIdleService extends Thread {

        private Robot robot;
        private double threshHold = 0.05;
        private int activeTime;
        private int idleTime;
        private boolean idle;
        private Rectangle screenDimenstions;

        MessageService messageService = new MessageService();

        public WinIdleService(int activeTime, int idleTime) {
            this.activeTime = activeTime;
            this.idleTime = idleTime;

            // Get the screen dimensions
            // MultiMonitor support.
            int screenWidth = 0;
            int screenHeight = 0;

            GraphicsEnvironment graphicsEnv = GraphicsEnvironment
                    .getLocalGraphicsEnvironment();
            GraphicsDevice[] graphicsDevices = graphicsEnv.getScreenDevices();

            for (GraphicsDevice screens : graphicsDevices) {
                DisplayMode mode = screens.getDisplayMode();
                screenWidth += mode.getWidth();

                if (mode.getHeight() > screenHeight) {
                    screenHeight = mode.getHeight();
                }
            }

            screenDimenstions = new Rectangle(0, 0, screenWidth, screenHeight);

            // setup the robot.
            robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e1) {
                e1.printStackTrace();
            }

            idle = false;
        }

        public void run() {
            while (true) {
                BufferedImage screenShot = robot
                        .createScreenCapture(screenDimenstions);

                try {
                    Thread.sleep(idle ? idleTime : activeTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                BufferedImage screenShot2 = robot
                        .createScreenCapture(screenDimenstions);


                idle = false;
                String state = "active";
                //System.out.println("machine active");

                if (compareScreens(screenShot, screenShot2) < threshHold) {
                    idle = true;
                    state = "idle";
                    //System.out.println("machine idle");
                }

                try {
                    messageService.publish(state);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        private double compareScreens(BufferedImage screen1, BufferedImage screen2) {
            int counter = 0;
            boolean changed = false;

            // Count the amount of change.
            for (int i = 0; i < screen1.getWidth() && !changed; i++) {
                for (int j = 0; j < screen1.getHeight(); j++) {
                    if (screen1.getRGB(i, j) != screen2.getRGB(i, j)) {
                        counter++;
                    }
                }
            }

            return (double) counter
                    / (double) (screen1.getHeight() * screen1.getWidth()) * 100;
        }

}
