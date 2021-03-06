package ui.gui.mainwindow.component.linelabels;

import ui.DataGUI;
import ui.gui.mainwindow.component.GlobalCoordinateSystem;
import ui.gui.mainwindow.component.GraphicalLine;

import java.awt.*;

// A linelabel, below the line, which is a square icon, which is (percentage) along the line, and is drawn
//      with simple primitives (i.e. drawn with a GRAPHICS2d object)
public abstract class SquareLineLabel extends LineLabel {
    private double percentage;

    public SquareLineLabel(GraphicalLine line, double percentage) {
        super(line);
        this.percentage = percentage;
        this.boundingX = DataGUI.CONSTRAINT_SIZE;
        this.boundingY = DataGUI.CONSTRAINT_SIZE;
    }

    @Override
    protected boolean isAbove() {
        return false;
    }


    // Simple square hitbox
    @Override
    public boolean inHitbox(int x, int y) {
        return  (x >= getTopCenterX() - DataGUI.CONSTRAINT_SIZE / 2 - DataGUI.CLICK_TOLERANCE)
                && (x <= getTopCenterX() + DataGUI.CONSTRAINT_SIZE / 2 + DataGUI.CLICK_TOLERANCE)
                && (y >= getTopCenterY() - DataGUI.CLICK_TOLERANCE)
                && (y <= getTopCenterY() + DataGUI.CONSTRAINT_SIZE + DataGUI.CLICK_TOLERANCE);
    }

    @Override
    protected void drawNotSelected(Graphics2D g, GlobalCoordinateSystem gcs) {
        int originx = gcs.getVirtualX();
        int originy = gcs.getVirtualY();
        drawBoundingBox(g, originx, originy, 0);
        drawIcon(g, originx + getTopLeftX(), originy + getTopLeftY());
    }

    @Override
    protected void drawSelected(Graphics2D g, GlobalCoordinateSystem gcs) {
        int originx = gcs.getVirtualX();
        int originy = gcs.getVirtualY();
        Stroke defaultStroke = g.getStroke();
        g.setStroke(DataGUI.bigStroke);
        drawBoundingBox(g, originx, originy, DataGUI.BIG_STROKE_WIDTH / 2);
        g.setStroke(defaultStroke);
        drawIcon(g, originx + getTopLeftX(), originy + getTopLeftY());
    }


    protected void drawBoundingBox(Graphics2D g, int originx, int originy, int offsetForStroke) {
        g.drawRect(getTopLeftX() + originx - offsetForStroke,
                getTopCenterY() + originy - offsetForStroke,
                DataGUI.CONSTRAINT_SIZE + 2 * offsetForStroke,
                DataGUI.CONSTRAINT_SIZE + 2 * offsetForStroke);
    }



    public abstract void drawIcon(Graphics2D g, int topLeftX, int topLeftY);


    // TODO: Make this be offset by a constant amount from percentage point on line, in the direction of it's normal
    // Gets the x coordinate (screen) of the top-middle part of the box
    protected int getTopCenterX() {
        return line.getMidpointX(percentage);
    }

    // Gets the y coordinate (screen) of the top-middle part of the box
    protected  int getTopCenterY() {
        if (isAbove()) {
            return line.getMidpointY(percentage) - DataGUI.CONSTRAINT_OFFSET - DataGUI.CONSTRAINT_SIZE;
        } else {
            return line.getMidpointY(percentage) + DataGUI.CONSTRAINT_OFFSET;
        }
    }

    // TODO: In the next big refactor, I can get rid of these functions. They (should) be replaced with coordX, coordY
    //          as they can be made to mean the same thing.
    protected int getTopLeftX() {
        return getTopCenterX() - DataGUI.CONSTRAINT_SIZE / 2;
    }

    protected int getTopLeftY() {
        return getTopCenterY();
    }

    @Override
    public void recompute() {
        this.coordX = getTopLeftX();
        this.coordY = getTopLeftY();
    }


    // TODO: A lot of the icon drawing stuff can be abstracted here. Icon-coords are essentially just percentages
    //          within the icon, implement some iconx(origin, percentage) here.

}
