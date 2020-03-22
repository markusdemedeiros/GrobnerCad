package ui.gui.mainwindow.component.linelabels;

import model.algebraic.Constraint;
import model.algebraic.PPHorizontalConstraint;
import model.geometric.Point;
import ui.DataGUI;
import ui.gui.mainwindow.component.Drawable;
import ui.gui.mainwindow.component.DrawableConstraint;
import ui.gui.mainwindow.component.GraphicalLine;

import java.awt.*;
import java.util.HashMap;

public class ConstraintHorizontalLineLabel extends SquareLineLabel implements DrawableConstraint {

    public static final double POSITION_PERCENTAGE = 0.33;

    public ConstraintHorizontalLineLabel(GraphicalLine line) {
        super(line, POSITION_PERCENTAGE);
    }

    @Override
    public void drawIcon(Graphics2D g, int topLeftX, int topLeftY) {
        g.drawLine(topLeftX + DataGUI.CONSTRAINT_INSET,
                topLeftY + DataGUI.CONSTRAINT_INSET,
                topLeftX + DataGUI.CONSTRAINT_INSET,
                topLeftY + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET);
        g.drawLine(topLeftX + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET,
                topLeftY + DataGUI.CONSTRAINT_INSET,
                topLeftX + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET,
                topLeftY + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET);
        g.drawLine(topLeftX + DataGUI.CONSTRAINT_INSET,
                topLeftY + DataGUI.CONSTRAINT_SIZE / 2,
                topLeftX + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET,
                topLeftY + DataGUI.CONSTRAINT_SIZE / 2);
    }

    @Override
    public String getType() {
        return Constraint.PP_HORIZONTAL_TYPE;
    }

    @Override
    public Constraint makePure(HashMap<Drawable, Point> pointDict, String name) {
        return new PPHorizontalConstraint(name, pointDict.get(line.getP1()), pointDict.get(line.getP2()));
    }
}
