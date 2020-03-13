package ui.gui.mainwindow.component;

import java.awt.*;

public class TestLineLabel extends LineLabel {
    public TestLineLabel(GraphicalLine line) {
        super(line);
    }

    @Override
    protected void drawSelected(Graphics2D g, int originx, int originy) {
        g.drawString("TEST", originx + line.getMidpointX(0.25), originy + line.getMidpointY(0.25));
    }

    @Override
    protected void drawNotSelected(Graphics2D g, int originx, int originy) {
        drawSelected(g, originx, originy);
    }

    @Override
    public String getType() {
        return "LINE 1";
    }
}
