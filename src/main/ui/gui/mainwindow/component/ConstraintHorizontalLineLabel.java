package ui.gui.mainwindow.component;

import model.algebraic.Constraint;
import ui.DataGUI;

import java.awt.*;

public class ConstraintHorizontalLineLabel extends SquareLineLabel {

    public static final double POSITION_PERCENTAGE = 0.33;

    public ConstraintHorizontalLineLabel(GraphicalLine line) {
        super(line, POSITION_PERCENTAGE);
    }

    @Override
    public void drawIcon(Graphics2D g, int topLeftX, int topLeftY) {
        g.drawLine(topLeftX + 2, topLeftY + 2, topLeftX + 2, topLeftY + DataGUI.CONSTRAINT_SIZE - 2);
        g.drawLine(topLeftX + DataGUI.CONSTRAINT_SIZE - 2, topLeftY + 2, topLeftX + DataGUI.CONSTRAINT_SIZE - 2, topLeftY + DataGUI.CONSTRAINT_SIZE - 2);
        g.drawLine(topLeftX + 2, topLeftY + DataGUI.CONSTRAINT_SIZE / 2, topLeftX + DataGUI.CONSTRAINT_SIZE - 2, topLeftY + DataGUI.CONSTRAINT_SIZE / 2);
    }

    @Override
    public String getType() {
        return Constraint.PP_HORIZONTAL_TYPE;
    }
}
