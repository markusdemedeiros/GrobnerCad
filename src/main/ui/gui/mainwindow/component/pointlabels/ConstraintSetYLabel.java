package ui.gui.mainwindow.component.pointlabels;

import model.algebraic.Constraint;
import ui.DataGUI;
import ui.gui.mainwindow.component.GraphicalPoint;

import java.awt.*;

public class ConstraintSetYLabel extends SquarePointLabel {
    private double yval;

    public ConstraintSetYLabel(GraphicalPoint point, double yval) {
        super(point, PointPosition.SOUTHEAST);
        this.yval = yval;
    }

    @Override
    public void drawIcon(Graphics2D g, int topLeftX, int topLeftY) {
        int xleft = topLeftX + DataGUI.CONSTRAINT_INSET;
        int xright = topLeftX + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET;
        int xmid = topLeftX + DataGUI.CONSTRAINT_SIZE / 2;
        int ytop = topLeftY + DataGUI.CONSTRAINT_INSET;
        int ybot = topLeftY + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET;
        int ymid = topLeftY + DataGUI.CONSTRAINT_SIZE / 2;
        int textFloor = topLeftY + DataGUI.CONSTRAINT_SIZE;
        g.drawLine(xleft, ytop, xmid, ymid);
        g.drawLine(xmid, ymid, xright, ytop);
        g.drawLine(xmid, ymid, xmid, ybot);
        g.drawString(Double.toString(yval), xright + DataGUI.CONSTRAINT_TEXT_SPACE, textFloor);
    }

    @Override
    public String getType() {
        return Constraint.P_SETY_CONSTRAINT;
    }


}
