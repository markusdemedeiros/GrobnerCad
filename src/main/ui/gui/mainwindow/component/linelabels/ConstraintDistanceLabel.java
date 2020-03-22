package ui.gui.mainwindow.component.linelabels;

import model.algebraic.Constraint;
import model.algebraic.PPDistanceConstraint;
import model.geometric.Point;
import ui.DataGUI;
import ui.gui.mainwindow.component.Drawable;
import ui.gui.mainwindow.component.DrawableConstraint;
import ui.gui.mainwindow.component.GraphicalLine;

import java.awt.*;
import java.util.HashMap;

public class ConstraintDistanceLabel extends SquareLineLabel implements DrawableConstraint {
    private double dist;

    public ConstraintDistanceLabel(GraphicalLine line, double dist) {
        super(line, 0.5);
        this.dist = dist;
    }

    @Override
    public void drawIcon(Graphics2D g, int topLeftX, int topLeftY) {
        // TODO: This should be abstracted into SquareLineLabel as general function, or a new IconCoordinate object
        int xleft = topLeftX + DataGUI.CONSTRAINT_INSET;
        int xright = topLeftX + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET;
        int xmid = topLeftX + 3 * DataGUI.CONSTRAINT_SIZE / 4;

        int ytop = topLeftY + DataGUI.CONSTRAINT_INSET;
        int ybot = topLeftY + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET;
        int ymid1 = topLeftY + DataGUI.CONSTRAINT_SIZE / 3;
        int ymid2 = topLeftY + 2 * DataGUI.CONSTRAINT_SIZE / 3;

        int textFloor = topLeftY + DataGUI.CONSTRAINT_SIZE;

        g.drawLine(xleft,  ytop, xleft, ybot);
        g.drawLine(xleft,  ytop, xmid,  ytop);
        g.drawLine(xleft, ybot, xmid, ybot);
        g.drawLine(xmid,  ytop, xright, ymid1);
        g.drawLine(xmid, ybot, xright, ymid2);
        g.drawLine(xright, ymid1, xright, ymid2);

        g.drawString(Double.toString(dist), xright + DataGUI.CONSTRAINT_TEXT_SPACE, textFloor);
    }

    @Override
    public String getType() {
        return Constraint.PP_DISTANCE_TYPE;
    }

    @Override
    protected boolean isAbove() {
        return true;
    }

    @Override
    public Constraint makePure(HashMap<Drawable, Point> pointDict, String name) {
        return new PPDistanceConstraint(name, pointDict.get(line.getP1()), pointDict.get(line.getP2()), dist);
    }
}
