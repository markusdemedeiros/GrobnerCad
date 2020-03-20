package ui.gui.mainwindow.component.pointlabels;

import ui.DataGUI;
import ui.gui.mainwindow.component.GraphicalPoint;

import java.awt.*;

public class ConstraintSetXLabel extends SquarePointLabel {
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
        return "POINT LABEL";
    }


}
