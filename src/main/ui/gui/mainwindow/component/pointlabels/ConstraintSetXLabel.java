package ui.gui.mainwindow.component.pointlabels;

import model.algebraic.Constraint;
import model.algebraic.PSetXConstraint;
import model.geometric.Point;
import ui.DataGUI;
import ui.gui.mainwindow.component.Drawable;
import ui.gui.mainwindow.component.DrawableConstraint;
import ui.gui.mainwindow.component.GraphicalPoint;

import java.awt.*;
import java.util.HashMap;

public class ConstraintSetXLabel extends SquarePointLabel implements DrawableConstraint {
    private double xval;

    public ConstraintSetXLabel(GraphicalPoint point, double xval) {
        super(point, PointPosition.NORTHEAST);
        this.xval = xval;
    }

    @Override
    public void drawIcon(Graphics2D g, int topLeftX, int topLeftY) {
        int xleft = topLeftX + DataGUI.CONSTRAINT_INSET;
        int xright = topLeftX + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET;
        int ytop = topLeftY + DataGUI.CONSTRAINT_INSET;
        int ybot = topLeftY + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET;
        int textFloor = topLeftY + DataGUI.CONSTRAINT_SIZE;

        g.drawLine(xleft, ytop, xright, ybot);
        g.drawLine(xleft, ybot, xright, ytop);

        g.drawString(Double.toString(xval), xright + DataGUI.CONSTRAINT_TEXT_SPACE, textFloor);
    }

    @Override
    public String getType() {
        return Constraint.P_SETX_CONSTRAINT;
    }


    @Override
    public Constraint makePure(HashMap<Drawable, Point> pointDict, String name) {
        return new PSetXConstraint(name, pointDict.get(point), xval);
    }
}
