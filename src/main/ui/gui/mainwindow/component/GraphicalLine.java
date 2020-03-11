package ui.gui.mainwindow.component;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import ui.DataGUI;

import java.awt.*;

// Represents a graphical line from coordinates (ax, ay) to (bx, by)
public class GraphicalLine extends Drawable {

    private GraphicalPoint p1;
    private GraphicalPoint p2;

    public GraphicalLine(GraphicalPoint p1, GraphicalPoint p2) {
        this.p1 = p1;
        this.p2 = p2;

        this.boundingX = p2.getVirtualCenterX() - p1.getVirtualCenterX();
        this.boundingY = p2.getVirtualCenterY() - p1.getVirtualCenterY();
    }


    @Override
    protected void drawSelected(Graphics2D g, int originx, int originy) {
        Stroke prevStroke = g.getStroke();
        g.setStroke(DataGUI.dashedStroke);
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
        // TEMP
        return (p1.getVirtualCenterX() < x) && (x < p2.getVirtualCenterX()) && (p1.getVirtualCenterY() < y) & (y < p2.getVirtualCenterY());
    }
}
