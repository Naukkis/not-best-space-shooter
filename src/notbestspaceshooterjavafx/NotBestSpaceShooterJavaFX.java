package notbestspaceshooterjavafx;

/**
 *
 * @author naukkis
 */

import notbestspaceshooterjavafx.gui.Window;
import javafx.application.Application;
import static javafx.application.Application.launch;


import javafx.stage.Stage;

public class NotBestSpaceShooterJavaFX extends Application {

    static Window window;
    static GameEngine engine;

    @Override
    public void start(Stage primaryStage) throws Exception {
           
        engine = new GameEngine();
        window = new Window(engine);
        window.start(primaryStage);
        
        engine.setWindow(window);
        engine.setDrawingBoard(window.getDrawer());
    }
     
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        engine.closeDatabase();
        super.stop();
    }  
}
