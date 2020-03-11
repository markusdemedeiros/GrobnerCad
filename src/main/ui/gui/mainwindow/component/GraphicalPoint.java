package ui.gui.mainwindow.component;

import javafx.scene.shape.Circle;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import static java.lang.StrictMath.pow;

public class GraphicalPoint extends Drawable {

    int radius = 5;
    int clickableRadius = radius + 10; // Because clickable is far too small

    public GraphicalPoint() {
        boundingX = 2 * radius;
        boundingY = 2 * radius;
    }


    // Circle inside the bounding box
    @Override
    public void drawSelected(Graphics2D g2d, int originx, int originy) {
        Ellipse2D shape = new Ellipse2D.Double(coordX + originx - radius, coordY + originy - radius, 2 * radius, 2 * radius);
        g2d.fill(shape);
    }

    @Override
    protected void drawNotSelected(Graphics2D g2d, int originx, int originy) {
        Ellipse2D shape = new Ellipse2D.Double(coordX + originx - radius, coordY + originy - radius, 2 * radius, 2 * radius);
        g2d.draw(shape);
    }

    @Override
    public boolean inHitbox(int x, int y) {
        // Change this to an array of true/false in general? Double operations are kinda overkill and memory is cheap
        double dist = Math.sqrt(pow(x - getVirtualCenterX(),2) + pow(y - getVirtualCenterY(), 2));
        return dist <= clickableRadius;
    }

    public int getVirtualCenterX() {
        return coordX;
    }

    public int getVirtualCenterY() {
        return coordY;
    }
}
