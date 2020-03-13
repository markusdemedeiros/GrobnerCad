package ui.gui.mainwindow.component;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import ui.DataGUI;

import java.awt.*;

// Represents a graphical line from coordinates (ax, ay) to (bx, by)
public class GraphicalLine extends Drawable {

    private GraphicalPoint p1;
    private GraphicalPoint p2;

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
       return (coordX - DataGUI.CLICK_TOLERANCE <= x)
               && (x <= coordX + boundingX + DataGUI.CLICK_TOLERANCE)
               && (Math.abs(slope * x + lineYIntercept - y) <= DataGUI.CLICK_TOLERANCE);
    }

    @Override
    public String getType() {
        return LINE_ID;
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

    @Override
    public void recompute() {
        recomputeCoords();
    }

    @Override
    public boolean isMoveable() {
        return false;
    }
}
