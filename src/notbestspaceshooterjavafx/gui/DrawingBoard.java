package notbestspaceshooterjavafx.gui;

import notbestspaceshooterjavafx.objects.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import notbestspaceshooterjavafx.GameEngine;

/**
 *
 * @author naukkis
 */
public final class DrawingBoard extends Canvas {

    private final Image backgroundImage;
    private Player player;
    private final GameEngine engine;
    private final Image start;
    private final Image highscore;
    private final Image quit;
    private final Image soundOn;
    private final Image soundOff;

    private int backGroundLocation;
    private int secondLoopLocation;

    public DrawingBoard(double width, double height, GameEngine engine) {
        super(width, height);
        this.engine = engine;
        this.backgroundImage = new Image("file:resources/violetspace.png");
        this.start = new Image("file:resources/start.png");
        this.highscore = new Image("file:resources/highscores.png");
        this.quit = new Image("file:resources/quit.png");
        this.soundOn = new Image("file:resources/soundOn.png");
        this.soundOff = new Image("file:resources/soundOff.png");

        this.backGroundLocation = -1660;
        this.secondLoopLocation = -4220;

        canvasHandler();
        this.player = this.engine.getPlayer();
    }

    public void canvasHandler() {

        new AnimationTimer() {
            int imageLocation = 0;
            long lastFrame = 0;
            long lastGameUpdate = 0;
            GraphicsContext gc = getGraphicsContext2D();

            @Override
            public void handle(long now) {

                if (now - lastFrame < 10000000) {
                    return;
                }
                gc.clearRect(0, 0, 600, 900);

                switch (engine.gameState()) {
                    case MAINMENU:
                        drawStaticBackgroundImage();
                        drawMainMenu();
                        break;
                    case GAME:
                        if (now - lastGameUpdate > 20000000) {
                            engine.update();
                            engine.checkCollisionsAndRemoveVoid();
                            engine.enemyWave();
                            this.lastGameUpdate = now;
                        }
                        drawMovingBackgroundImage();
                        drawPlayerImage();
                        drawEnemies();
                        drawAmmos();

                        drawScore();
                        break;

                    case PAUSE:
                        drawStaticBackgroundImage();
                        drawPlayerImage();
                        drawScore();
                        gc.setFill(Color.CORAL);
                        drawMainMenu();
                        break;
                }

                imageLocation += 1;
                this.lastFrame = now;
                }

            private void drawMainMenu() {
                gc.drawImage(start, 200, 200);
                gc.drawImage(highscore, 200, 250);
                if (engine.soundON()) {
                    gc.drawImage(soundOn, 200, 300);
                } else {
                    gc.drawImage(soundOff, 200, 300);
                    }
                gc.drawImage(quit, 200, 350);
                }

            private void drawScore() {
                gc.setFill(Color.WHITE);
                String score = Integer.toString(player.getScore());
                gc.fillText(score, 10, 10);
            }

            private void drawStaticBackgroundImage() {
                gc.drawImage(backgroundImage, -20, backGroundLocation);
                gc.drawImage(backgroundImage, -20, secondLoopLocation);
            }

            private void drawPlayerImage() {
                gc.drawImage(player.getImage(), player.getX(), player.getY());
            }

            private void drawMovingBackgroundImage() {
                gc.drawImage(backgroundImage, -20, movingBackgroundY());
                gc.drawImage(backgroundImage, -20, secondLoopLocation);
            }
            private int movingBackgroundY() {
                if (secondLoopLocation == -1) {
                    backGroundLocation = -2560;
                }
                if (secondLoopLocation > 900) {
                    secondLoopLocation = -4220;
                }
                backGroundLocation += 1;
                secondLoopLocation += 1;
                return backGroundLocation;
            }

            private void drawAmmos() {
                if (engine.getAmmos() != null) {
                    gc.setFill(Color.WHITE);
                    for (int i = 0; i < engine.getAmmos().size(); i++) {
                        gc.fillOval(engine.getAmmos().get(i).getX(), engine.getAmmos().get(i).getY(), engine.getAmmos().get(i).getSize(), engine.getAmmos().get(i).getSize());
                    }
                }
            }
            
            private void drawEnemies() {
                gc.setFill(Color.CORAL);
                for (int i = 0; i < engine.getEnemies().size(); i++) {
                    gc.fillOval(engine.getEnemies().get(i).getX(), engine.getEnemies().get(i).getY(), engine.getEnemies().get(i).getSize(), engine.getEnemies().get(i).getSize());
                }
            }
        }.start();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
