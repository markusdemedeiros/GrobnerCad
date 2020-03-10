package ui.gui.mainwindow.component;

import javafx.scene.shape.Circle;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import static java.lang.StrictMath.pow;

public class ExampleDrawableCircle extends Drawable {

    int radius = 20;

    public ExampleDrawableCircle() {
        boundingX = 2 * radius;
        boundingY = 2 * radius;
    }


    // Circle inside the bounding box
    @Override
    public void drawSelected(Graphics2D g2d, int originx, int originy) {
        Ellipse2D shape = new Ellipse2D.Double(coordX + originx, coordX + originy, 2 * radius, 2 * radius);
        g2d.fill(shape);
    }

    @Override
    protected void drawNotSelected(Graphics2D g2d, int originx, int originy) {
        Ellipse2D shape = new Ellipse2D.Double(coordX + originx, coordX + originy, 2 * radius, 2 * radius);
        g2d.draw(shape);
    }

    @Override
    public boolean inHitbox(int x, int y) {
        // Change this to an array of true/false in general? Double operations are kinda overkill and memory is cheap
        double dist = Math.sqrt(pow(x - coordX - radius,2) + pow(y - coordX - radius, 2));
        return dist <= radius;
    }
}
