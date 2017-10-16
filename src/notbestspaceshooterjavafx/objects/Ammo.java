package notbestspaceshooterjavafx.objects;

import java.awt.Rectangle;

public class Ammo implements UpdatableObject {

    private int x, y, speed, size;

    public Ammo(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 20;
        this.size = 10;
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
       this.setY(this.getY() - 20);
    }

    @Override
    public String toString() {
        return "ammo";
    }
    
    
}
