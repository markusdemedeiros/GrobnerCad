package ui.gui.mainwindow.component;

import ui.DataGUI;

import javax.xml.crypto.Data;
import java.awt.*;

public class ConstraintHorizontalLineLabel extends SquareLineLabel {
    public ConstraintHorizontalLineLabel(GraphicalLine line, double percentage) {
        super(line, percentage);
    }

    @Override
    public void drawIcon(Graphics2D g, int topLeftX, int topLeftY) {
        g.drawLine(topLeftX, topLeftY, topLeftX + DataGUI.CONSTRAINT_SIZE, topLeftY + DataGUI.CONSTRAINT_SIZE);
    }


    @Override
    public String getType() {
        return "LINE 1";
    }


}
