package notbestspaceshooterjavafx.gui;

import java.util.ArrayList;
import java.util.Optional;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import notbestspaceshooterjavafx.GameEngine;
import notbestspaceshooterjavafx.GameState;

/**
 *
 * @author naukkis
 */
public class Window extends Application {

    private GameEngine engine;
    static ArrayList<String> input = new ArrayList<>();
    private DrawingBoard drawingBoard;
    private boolean up, right, down, left;
    private AnimationTimer timer;

    public Window(GameEngine engine) {
        this.engine = engine;
    }

    @Override
    public void start(Stage stage) {

        BorderPane borderLayout = new BorderPane();
        this.drawingBoard = new DrawingBoard(600, 900, engine);
        borderLayout.setCenter(drawingBoard);
        Group root = new Group();
        Scene scene = new Scene(root, 600, 900, Color.BLACK);

        root.getChildren().add(borderLayout);

        stage.setTitle("Not Best Space Shooter");
        stage.setScene(scene);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String code = event.getCode().toString();

                if (code.equals("LEFT")) {
                    left = true;
                }
                if (code.equals("RIGHT")) {
                    right = true;
                }
                if (code.equals("UP")) {
                    up = true;
                }
                if (code.equals("DOWN")) {
                    down = true;
                }
            }
        }
        );
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String code = event.getCode().toString();
                if (engine.gameState() == GameState.GAME) {
                    timer.start();
                    if (code.equals("LEFT")) {
                        left = false;
                    }
                    if (code.equals("RIGHT")) {
                        right = false;
                    }
                    if (code.equals("UP")) {
                        up = false;
                    }
                    if (code.equals("DOWN")) {
                        down = false;
                    }
                    if (code.equals("SPACE")) {
                        engine.playFireSound();
                        engine.shoot();
                    }
                }
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (engine.gameState() != GameState.GAME) {
                    this.stop();
                }
                int deltaX = 0, deltaY = 0;

                if (left) {
                    deltaX -= 3;
                }
                if (right) {
                    deltaX += 3;
                }
                if (up) {
                    deltaY -= 3;
                }
                if (down) {
                    deltaY += 3;
                }

                engine.movePlayer(deltaX, deltaY);
            }
        };
        scene.setOnMouseClicked((MouseEvent e) -> {

            if (engine.gameState() == GameState.MAINMENU || engine.gameState() == GameState.PAUSE) {

                if (e.getX() > 200 && e.getX() < 400) {
                    if (e.getY() > 200 && e.getY() < 250) {
                        this.engine.restart();
                    } else if (e.getY() > 250 && e.getY() < 300) {
                        Alert alert = getHighscorelist();
                        alert.showAndWait();
                    } else if (e.getY() > 300 && e.getY() < 350) {
                        this.engine.setSoundONOFF();
                    } else if (e.getY() > 350 && e.getY() < 400) {
                        Alert alert = getQuitConfirmDialog();
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            System.exit(0);
                        }
                    }
                }
            }
        }
        );

        stage.show();
    }

    public Alert getQuitConfirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Quit?");
        alert.setContentText("Want to quit, really?");
        return alert;
    }

    private Alert getHighscorelist() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("High Scores");
        alert.setHeaderText("High Scores");
        alert.setContentText(engine.getHighScores());
        return alert;
    }

    public void highscoreDialog() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("New high score");
                dialog.setHeaderText("You made it to top 10");
                dialog.setContentText("Please enter your name:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> engine.insertUserToDatabase(name));
            }
        });
        getHighscorelist();
    }

    public DrawingBoard getDrawer() {
        return drawingBoard;
    }

    public ArrayList<String> getInput() {
        return input;
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void reset() {
        left = false;
        right = false;
        up = false;
        down = false;
    }
}
