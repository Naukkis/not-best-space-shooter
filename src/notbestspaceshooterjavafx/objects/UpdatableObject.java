
package notbestspaceshooterjavafx.objects;

import java.awt.Rectangle;

public interface UpdatableObject {
    void update();
    int getY();
    int getX();
    int getSize();
    Rectangle getBounds();
}
