package ui.gui.mainwindow.component;

import javax.swing.*;

// This class is meant as a translator between "virtual coodinates" and "screen coordinates". Screen coordinates are the
//      literal pixels in the Jpanel, and virtual coordinates are the poisitions of objects relative to the origin.
//  For example, when the screen pans around the method in which these are converted has to change! This class handles
//      all of that accounting.

public class GlobalCoordinateSystem {

    private JPanel parent;

    private int virtualX;
    private int virtualY;

    public GlobalCoordinateSystem(JPanel parent) {
        this.parent = parent;
        this.virtualY = 0;
        this.virtualX = 0;
    }

    public int getVirtualX() {
        return virtualX;
    }

    public int getVirtualY() {
        return virtualY;
    }

    public void shiftCoordinates(int dx, int dy) {
        virtualX += dx;
        virtualY += dy;
    }

    // TODO: Scale factor code in here!

    public int liftToScreenX(int virtualXValue) {
        return virtualXValue + virtualX;
    }

    public int liftToScreenY(int virtualYValue) {
        return virtualYValue + virtualY;
    }

    public int pushToVirtualX(int screenXvalue) {
        return screenXvalue - virtualX;
    }

    public int pushToVirtualY(int screenYvalue) {
        return screenYvalue - virtualY;
    }

    public int screenWidth() {
        return parent.getWidth();
    }

    public int screenHeight() {
        return parent.getHeight();
    }

    public boolean inScreen(int virtualXValue, int virtualYValue, int boundingX, int boundingY) {
        return ((liftToScreenX(virtualXValue + boundingX) >= 0)
                && (liftToScreenX(virtualXValue) <= screenWidth())
                && (liftToScreenY(virtualYValue + boundingY) >= 0)
                && (liftToScreenY(virtualYValue) <= screenHeight()));
    }
}
