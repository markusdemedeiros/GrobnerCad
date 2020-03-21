package ui.gui.mainwindow.component.linelabels;

import model.algebraic.Constraint;
import ui.DataGUI;
import ui.gui.mainwindow.component.GraphicalLine;

import java.awt.*;

public class ConstraintVerticalLineLabel extends SquareLineLabel {

    public static final double POSITION_PERCENTAGE = 0.66;

    public ConstraintVerticalLineLabel(GraphicalLine line) {
        super(line, POSITION_PERCENTAGE);
    }

    @Override
    public void drawIcon(Graphics2D g, int topLeftX, int topLeftY) {
        g.drawLine(topLeftX + DataGUI.CONSTRAINT_INSET,
                topLeftY + DataGUI.CONSTRAINT_INSET,
                topLeftX + DataGUI.CONSTRAINT_SIZE / 2,
                topLeftY + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET);
        g.drawLine(topLeftX + DataGUI.CONSTRAINT_SIZE / 2,
                topLeftY + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET,
                topLeftX + DataGUI.CONSTRAINT_SIZE - DataGUI.CONSTRAINT_INSET,
                topLeftY + DataGUI.CONSTRAINT_INSET);
    }

    @Override
    public String getType() {
        return Constraint.PP_VERTICAL_TYPE;
    }
}
