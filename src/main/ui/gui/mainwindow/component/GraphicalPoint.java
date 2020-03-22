package ui.gui.mainwindow.component;

import javafx.scene.shape.Circle;
import model.geometric.Geometry;
import ui.DataGUI;

import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.geom.Ellipse2D;

import static java.lang.StrictMath.pow;

public class GraphicalPoint extends Drawable {

    int radius = DataGUI.POINT_RADIUS;
    int clickableRadius = radius + DataGUI.CLICK_TOLERANCE;

    public GraphicalPoint() {
        boundingX = 2 * radius;
        boundingY = 2 * radius;
    }


    // Circle inside the bounding box
    @Override
    public void drawSelected(Graphics2D g2d, int originx, int originy) {
        g2d.draw(getOutline(originx, originy));
        g2d.fill(getOutline(originx, originy));
    }

    @Override
    protected void drawNotSelected(Graphics2D g2d, int originx, int originy) {
        g2d.draw(getOutline(originx, originy));
    }

    private Shape getOutline(int originx, int originy) {
        return new Ellipse2D.Double(coordX + originx - radius, coordY + originy - radius, 2 * radius, 2 * radius);
    }

    @Override
    public boolean inHitbox(int x, int y) {
        // Change this to an array of true/false in general? Double operations are kinda overkill and memory is cheap
        double dist = Math.sqrt(pow(x - getVirtualCenterX(),2) + pow(y - getVirtualCenterY(), 2));
        return dist <= clickableRadius;
    }

    @Override
    public String getType() {
        return Geometry.TYPE_POINT;
    }

    public int getVirtualCenterX() {
        return coordX;
    }

    public int getVirtualCenterY() {
        return coordY;
    }

    @Override
    public void recompute() {
        // Nothing to compute for a point, it's position depends on nothing at creation.
    }

    @Override
    public boolean isMoveable() {
        return true;
    }

}
