package ui.gui.mainwindow.component;

import model.algebraic.Constraint;
import model.geometric.Geometry;
import ui.DataGUI;

import java.awt.*;

public class ConstraintCoincidentLabel extends GraphicalLine {

    public ConstraintCoincidentLabel(GraphicalPoint p1, GraphicalPoint p2) {
        super(p1, p2);
    }

    // Constraints are not meoveable
    @Override
    public boolean isMoveable() {
        return false;
    }

    @Override
    public String getType() {
        return Constraint.PP_COINCIDENT_TYPE;
    }

    @Override
    public void drawNotSelected(Graphics2D g, int originx, int originy) {
        Stroke prevStroke = g.getStroke();
        g.setStroke(DataGUI.dashedStroke);
        g.drawLine(p1.getVirtualCenterX() + originx,
                p1.getVirtualCenterY()  + originy,
                p2.getVirtualCenterX() + originx,
                p2.getVirtualCenterY() + originy);
        g.setStroke(prevStroke);
    }

    @Override
    public void drawSelected(Graphics2D g, int originx, int originy) {
        Stroke prevStroke = g.getStroke();
        g.setStroke(DataGUI.bigDashedStroke);
        g.drawLine(p1.getVirtualCenterX() + originx,
                p1.getVirtualCenterY()  + originy,
                p2.getVirtualCenterX() + originx,
                p2.getVirtualCenterY() + originy);
        g.setStroke(prevStroke);
    }
}
