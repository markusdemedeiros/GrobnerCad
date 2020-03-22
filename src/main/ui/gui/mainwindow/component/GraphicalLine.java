package ui.gui.mainwindow.component;

import model.geometric.Geometry;
import ui.DataGUI;
import ui.gui.mainwindow.exceptions.ZeroSlopeException;

import java.awt.*;
import java.util.ArrayList;

// Represents a graphical line from coordinates (ax, ay) to (bx, by)
public class GraphicalLine extends Drawable {

    protected GraphicalPoint p1;
    protected GraphicalPoint p2;

    private double lineYIntercept;
    private double slope;


    public GraphicalLine(GraphicalPoint p1, GraphicalPoint p2) {
        this.p1 = p1;
        this.p2 = p2;

        // Line position is dependent on point position
        this.p1.addDependency(this);
        this.p2.addDependency(this);

        // Endpoint positions are dependent on line position
        this.addDependency(p1);
        this.addDependency(p2);
        recomputeCoords();
    }

    @Override
    public void updateToDraw(int xleft, int xright, int ytop, int ybot) {
        super.updateToDraw(xleft, xright, ytop, ybot);
    }

    @Override
    protected void drawSelected(Graphics2D g, int originx, int originy) {
        Stroke prevStroke = g.getStroke();
        g.setStroke(DataGUI.bigStroke);
        g.drawLine(p1.getVirtualCenterX() + originx,
                p1.getVirtualCenterY()  + originy,
                p2.getVirtualCenterX() + originx,
                p2.getVirtualCenterY() + originy);
        g.setStroke(prevStroke);
    }

    @Override
    protected void drawNotSelected(Graphics2D g, int originx, int originy) {
        g.drawLine(p1.getVirtualCenterX() + originx,
                p1.getVirtualCenterY()  + originy,
                p2.getVirtualCenterX() + originx,
                p2.getVirtualCenterY() + originy);
    }

    @Override
    public boolean inHitbox(int x, int y) {
        // This is true iff the click is betwen point edges, up to error
        return ((coordX - DataGUI.CLICK_TOLERANCE <= x)
               && (x <= coordX + boundingX + DataGUI.CLICK_TOLERANCE)
               && (Math.abs(slope * x + lineYIntercept - y) <= DataGUI.CLICK_TOLERANCE));
    }

    @Override
    public String getType() {
        return Geometry.TYPE_LINE;
    }


    // The coordinate (virtual pixels) is the top left corner of the bounding box.
    // This could potentially change based on the positions of the points when they move
    // This function recalculates this.
    // Must be called when internal structure changes.
    // TODO: Make this an abstract function which is called by in Drawable, so nobody could ever forget
    private void recomputeCoords() {
        int p1x = p1.getVirtualCenterX();
        int p2x = p2.getVirtualCenterX();
        int p1y = p1.getVirtualCenterY();
        int p2y = p2.getVirtualCenterY();

        // Get upper left corner
        coordX = Math.min(p1x, p2x);
        coordY = Math.min(p1y, p2y);

        // Get bounding box length, measured positive
        boundingX = Math.abs(p1x - p2x);
        boundingY = Math.abs(p1y - p2y);

        // Compute slope of line
        slope = Double.valueOf(p2y - p1y) / Double.valueOf(p2x - p1x);
        lineYIntercept = p1y - slope * p1x;
    }

    // Gets a midpoint of x% of the way through the line measured from the left
    public int getMidpointX(double percentage) {
        GraphicalPoint leftPoint;
        if (p1.getVirtualCenterX() <= p2.getVirtualCenterX()) {
            leftPoint = p1;
        } else {
            leftPoint = p2;
        }
        return (int) (leftPoint.getVirtualCenterX() + boundingX * percentage);
    }

    public int getMidpointY(double percentage) {
        GraphicalPoint leftPoint;
        if (p1.getVirtualCenterX() <= p2.getVirtualCenterX()) {
            leftPoint = p1;
        } else {
            leftPoint = p2;
        }

        return (int) (leftPoint.getVirtualCenterY() + boundingX * slope * percentage);
    }

    @Override
    public void recompute() {
        recomputeCoords();
    }

    @Override
    public boolean isMoveable() {
        return false;
    }

    // Gets the slope of the normal vector to the line
    public double getNormal() throws ZeroSlopeException {
        if (slope == 0) {
            throw new ZeroSlopeException();
        }
        return -1 / slope;
    }

    public ArrayList<GraphicalPoint> getEndpoints() {
        ArrayList<GraphicalPoint> output = new ArrayList<GraphicalPoint>();
        output.add(p1);
        output.add(p2);
        return output;
    }

    public GraphicalPoint getP1() {
        return p1;
    }

    public GraphicalPoint getP2() {
        return p2;
    }
}
