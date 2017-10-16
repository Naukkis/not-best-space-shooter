package notbestspaceshooterjavafx;

import notbestspaceshooterjavafx.gui.DrawingBoard;
import notbestspaceshooterjavafx.objects.UpdatableObject;
import notbestspaceshooterjavafx.objects.GenericEnemy;
import notbestspaceshooterjavafx.objects.Player;
import notbestspaceshooterjavafx.objects.Ammo;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import notbestspaceshooterjavafx.gui.Window;

public class GameEngine {

    private DrawingBoard drawingboard;
    private Window window;
    private Player player;
    private boolean soundONOFF;
    private int BOUNDARY_X, BOUNDARY_Y;
    private ArrayList<UpdatableObject> enemies;
    private ArrayList<UpdatableObject> ammos;
    private List<ArrayList<UpdatableObject>> updatableLists;
    DatabaseHandler dbhandler;
    private GameState state;

    public GameEngine() {
        this.player = new Player();
        this.enemies = new ArrayList();
        this.ammos = new ArrayList();
        this.updatableLists = new ArrayList();
        this.updatableLists.add(enemies);
        this.updatableLists.add(ammos);

        this.soundONOFF = false;
        this.BOUNDARY_X = 600;
        this.BOUNDARY_Y = 900;
        this.state = GameState.MAINMENU;
        this.dbhandler = new DatabaseHandler();
    }

    public void update() {
        for (ArrayList<UpdatableObject> objectsToUpdate : updatableLists) {
            for (UpdatableObject obj : objectsToUpdate) {
                obj.update();
            }
        }
    }

    public void enemyWave() {
        if (this.getEnemies().size() < 5) {
            this.spawnNewEnemy();
        }
        if (this.player.getScore() > 30 && this.getEnemies().size() < 10) {
            this.spawnNewEnemy();
        }
    }

    private void spawnNewEnemy() {
        Random random = new Random();
        GenericEnemy newEnemy = new GenericEnemy(random.nextInt(520) + 30, 0, 10, 20);
        for (UpdatableObject object : this.enemies) {
            if (collision(newEnemy.getBounds(), object.getBounds())) {
                newEnemy = null;
            }
        }
        if (newEnemy != null) {
            enemies.add(newEnemy);
        }

    }

    public void checkCollisionsAndRemoveVoid() {
        ArrayList<UpdatableObject> removeableEnemies = new ArrayList();
        ArrayList<UpdatableObject> removeableAmmos = new ArrayList();

        for (UpdatableObject enemy : this.enemies) {

            if (outOfBounds(enemy)) {
                removeableEnemies.add(enemy);
            } else {

                checkPlayerCollision(enemy);

                for (UpdatableObject ammo : this.ammos) {
                    if (outOfBounds(ammo)) {
                        removeableAmmos.add(ammo);
                    } else if (collision(enemy.getBounds(), ammo.getBounds())) {
                        removeableEnemies.add(enemy);
                        removeableAmmos.add(ammo);
                        player.setScore();
                    }
                }
            }

        }
        this.enemies.removeAll(removeableEnemies);
        this.ammos.removeAll(removeableAmmos);
    }

    private void checkPlayerCollision(UpdatableObject enemy) {
        if (collision(enemy.getBounds(), this.player.getBounds())) {
            this.player.setAlive(false);
            pauseGame();
            if (dbhandler.lastScoreOfTopTen() < this.player.getScore()) {
                window.highscoreDialog();
            }

        }
    }

    private boolean collision(Rectangle obj1, Rectangle obj2) {
        return obj1.intersects(obj2);
    }

    private boolean outOfBounds(UpdatableObject obj) {
        return obj.getY() < 0 | obj.getY() > BOUNDARY_Y;
    }

    public void shoot() {
        this.ammos.add(new Ammo(this.player.getX() + 35, this.player.getY() - 5));
        if (this.player.getScore() > 5 && this.player.getScore() < 10) {
            this.ammos.add(new Ammo(this.player.getX() + 3, this.player.getY() - 5));
            this.ammos.add(new Ammo(this.player.getX() + 65, this.player.getY() - 5));
        }

    }

    public void movePlayer(int deltaX, int deltaY) {
        if (this.player.getX() + deltaX < 0 || this.player.getX() + this.player.getSize() + deltaX > BOUNDARY_X) {
            deltaX = 0;
        }
        if (this.player.getY() + deltaY < 0 || this.player.getY() + this.player.getSize() + deltaY > BOUNDARY_Y) {
            deltaY = 0;
        }
        this.player.setX(this.player.getX() + deltaX);
        this.player.setY(this.player.getY() + deltaY);
    }

    public void restart() {
        this.player = new Player();
        this.enemies = new ArrayList();
        this.ammos = new ArrayList();
        this.updatableLists = new ArrayList();
        this.updatableLists.add(enemies);
        this.updatableLists.add(ammos);
        this.drawingboard.setPlayer(player);
        this.window.reset();
        startGame();
    }

    public void setDrawingBoard(DrawingBoard drawingboard) {
        this.drawingboard = drawingboard;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public void setBoundaries(int boundaryX, int boundaryY) {
        this.BOUNDARY_X = boundaryX;
        this.BOUNDARY_Y = boundaryY;
    }

    public void setSoundONOFF() {
        soundONOFF = !this.soundONOFF;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<UpdatableObject> getAmmos() {
        return ammos;
    }

    public ArrayList<UpdatableObject> getEnemies() {
        return enemies;
    }

    public void playFireSound() {
        if (this.soundONOFF) {
            SoundEffect soundEffects = new SoundEffect("gun.wav");
            soundEffects.playSound();
        }
    }

    public void pauseGame() {
        if (state == GameState.PAUSE) {
            state = GameState.GAME;
        } else {
            state = GameState.PAUSE;
        }

    }

    public void startGame() {
        state = GameState.GAME;
    }

    public void mainMenu() {
        state = GameState.MAINMENU;
    }

    public GameState gameState() {
        return state;
    }

    public boolean soundON() {
        return soundONOFF;
    }

    public String getHighScores() {
        String highscores = dbhandler.printTopTen();
        return highscores;
    }

    public void insertUserToDatabase(String name) {
        dbhandler.insertUser(name, this.player.getScore());
    }

    public void removeHighscore(int user) {
        dbhandler.removeHighScore(user);
    }

    public void closeDatabase() {
        dbhandler.closeDown();
    }
}
