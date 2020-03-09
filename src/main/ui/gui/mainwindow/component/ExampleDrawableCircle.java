package ui.gui.mainwindow.component;

import javafx.scene.shape.Circle;

import java.awt.*;

public class ExampleDrawableCircle extends Drawable {

    int radius = 20;

    public ExampleDrawableCircle() {
        boundingX = 2 * radius;
        boundingY = 2 * radius;
    }


    // Circle inside the bounding box
    @Override
    public void drawImage(Graphics2D g2d, int originx, int originy) {
        g2d.drawOval(coordX + originx,  coordY + originy,  2 * radius, 2 * radius);
    }

    @Override
    public boolean inHitbox(int x, int y) {
        return false;
    }
}
