package ui.gui.mainwindow.component;

import javafx.scene.shape.Circle;

import java.awt.*;

import static java.lang.StrictMath.pow;

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
        double dist = Math.sqrt(pow(x - coordX - radius,2) + pow(y - coordX - radius, 2));
        System.out.println(dist);
        return dist <= radius;
    }
}
