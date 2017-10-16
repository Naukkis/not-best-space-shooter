package notbestspaceshooterjavafx.objects;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GenericEnemy implements UpdatableObject {

    private int x, y, size, speed;
    private BufferedImage img;
    private BufferedImage dead;
    private boolean dodgingEnemy;
    private int dodgeMinHeight;
    private int dodgeMaxHeight;

    public GenericEnemy(int x, int y, int speed, int size) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.size = size;
        Random random = new Random();
        if (random.nextInt(50) > 25) {
            dodgingEnemy = true;
        } else {
            dodgingEnemy = false;
        }
        this.dodgeMinHeight = random.nextInt(200) + 50;
        this.dodgeMaxHeight = random.nextInt(500) + 100;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSize() {
        return size;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    @Override
    public void update() {
        this.setY(this.getY() + this.getSpeed());
        if (dodgingEnemy) {
            if (y > dodgeMinHeight && !(y > dodgeMaxHeight)) {
                this.setX(x + 5);
            }
            if (y > 500) {
                this.setX(x - 5);
            }

        }

    }

    @Override
    public String toString() {
        return "enemy";
    }
}
