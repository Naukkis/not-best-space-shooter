package notbestspaceshooterjavafx.objects;

import java.awt.Rectangle;
import javafx.scene.image.Image;

public class Player {

    private int x, y, size, score, speed;
    private boolean alive;
    private Image img;
    private Image dead;

    public Player() {
        this.x = 300;
        this.y = 700;
        this.size = 100;
        this.alive = true;
        this.score = 0;
        this.speed = 10;

        this.img = new Image("file:resources/spaceship.png");
        this.dead = new Image("file:resources/dead.png");
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getScore() {
        return score;
    }

    public int getSpeed() {
        return speed;
    }
    
    public void setScore() {
        this.score += 1;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public Image getImage() {
        if (alive) {
            return img;
        }
        return dead;
    }

}
