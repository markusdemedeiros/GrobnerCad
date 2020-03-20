package ui.gui.mainwindow.component.pointlabels;

import ui.DataGUI;
import ui.gui.mainwindow.component.GraphicalPoint;

import javax.xml.crypto.Data;
import java.awt.*;

// TODO: A lot of this stuff is similar to SquareLineLabel. Extract the squareness properties to new class
public abstract class SquarePointLabel extends PointLabel {
    public SquarePointLabel(GraphicalPoint point, PointPosition position) {
        super(point, position);
    }

    // Simple square hitbox
    @Override
    public boolean inHitbox(int x, int y) {
        return  (x >= getTopCenterX() - DataGUI.CONSTRAINT_SIZE / 2 - DataGUI.CLICK_TOLERANCE) &&
                (x <= getTopCenterX() + DataGUI.CONSTRAINT_SIZE / 2 + DataGUI.CLICK_TOLERANCE) &&
                (y >= getTopCenterY() - DataGUI.CLICK_TOLERANCE) &&
                (y <= getTopCenterY() + DataGUI.CONSTRAINT_SIZE + DataGUI.CLICK_TOLERANCE);
    }

    @Override
    protected void drawNotSelected(Graphics2D g, int originx, int originy) {
        drawBoundingBox(g, originx, originy, 0);
        drawIcon(g, originx + coordX, originy + coordY);
    }

    @Override
    protected void drawSelected(Graphics2D g, int originx, int originy) {
        Stroke defaultStroke = g.getStroke();
        g.setStroke(DataGUI.bigStroke);
        drawBoundingBox(g, originx, originy, DataGUI.BIG_STROKE_WIDTH / 2);
        g.setStroke(defaultStroke);
        drawIcon(g, originx + coordX, originy + coordY);
    }


    protected void drawBoundingBox(Graphics2D g, int originx, int originy, int offsetForStroke) {
        g.drawRect(coordX + originx - offsetForStroke,
                getTopCenterY() + originy - offsetForStroke,
                DataGUI.CONSTRAINT_SIZE + 2 * offsetForStroke,
                DataGUI.CONSTRAINT_SIZE + 2 * offsetForStroke);
    }



    public abstract void drawIcon(Graphics2D g, int topLeftX, int topLeftY);


    // TODO: Make this be offset by a constant amount from percentage point on line, in the direction of it's normal
    // Gets the x coordinate (screen) of the top-middle part of the box
    protected int getTopCenterX() {
        return coordX + DataGUI.CONSTRAINT_SIZE / 2;
    }

    // Gets the y coordinate (screen) of the top-middle part of the box
    protected  int getTopCenterY() {
        return coordY;
    }

    // EFFECTS: Returns the amount of pixels in Y to offset the label from the point in X
    //              based on the position of the label
    @Override
    protected int positionalOffsetY() {
        switch (position) {
            case SOUTHEAST:
            case SOUTHWEST:
                return DataGUI.CONSTRAINT_OFFSET_POINT;
            case NORTHEAST:
            case NORTHWEST:
                return -DataGUI.CONSTRAINT_OFFSET_POINT - DataGUI.CONSTRAINT_SIZE;
            default:
                return 0;
        }
    }


    // EFFECTS: Returns the amount of pixels in X to offset the label from the point in X
    //              based on the position of the label
    @Override
    protected int positionalOffsetX() {
        switch (position) {
            case NORTHEAST:
            case SOUTHEAST:
                return DataGUI.CONSTRAINT_OFFSET_POINT;
            case NORTHWEST:
            case SOUTHWEST:
                return -DataGUI.CONSTRAINT_OFFSET_POINT - DataGUI.CONSTRAINT_SIZE;
            default:
                return 0;
        }
    }



}
