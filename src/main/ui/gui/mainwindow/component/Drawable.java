package ui.gui.mainwindow.component;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

// Any object (aside from background because that's an infinte collection)
//      which can be drawn on screen must implement this

// Needs to be able to be drawn, dragged/onclick action
public abstract class Drawable {
    // Draw variables
    //      Top left pixel is at (coordX, coordY),
    //      Object is bounded in box of length boundingX, boundingY
    protected int coordX = 0;      // Relative to plane origin
    protected int coordY = 0;
    protected int boundingX;
    protected int boundingY;
    private boolean toDraw;

    //      Objects will be drawn iff their bounding box intersects screen
    //      paramater coords are coordinates of screen boundaries
    public void updateToDraw(int xleft, int xright, int ytop, int ybot) {
        toDraw = ((coordX + boundingX >= xleft) &&      // Left side is inside screen
                (coordX <= xright) &&                   // Right side is inside screen
                (coordY + boundingY >= ytop) &&         // Bottom side is inside screen
                (coordY <= ybot));                      // Top side is inside screen
    }

    public boolean getToDraw() {
        return toDraw;
    }

    // Draws image to Graphics3D at it's internally stored coordinates
    public abstract void drawImage(Graphics2D g2d, int originx, int originy);



    // Mouse Interaction variables
    //      Returns true if a click with absolute coords x and y should count as a click
    public abstract boolean inHitbox(int x, int y);



    // Drag variables
    private boolean isBeingDragged = false;
    private int dragStartX;
    private int dragStartY;

    // Update position relative to plane origin
    public void addOffset(int offsetX, int offsetY) {
        this.coordX += offsetX;
        this.coordY += offsetY;
    }

    // Set absolute position of the object
    public void setOffset(int offsetX, int offsetY) {
        this.coordX = offsetX;
        this.coordY = offsetY;
    }

}
