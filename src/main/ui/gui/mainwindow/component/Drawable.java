package ui.gui.mainwindow.component;


import java.awt.*;
import java.util.ArrayList;

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
    private boolean isSelected;

    // These are the objects that must be recalculated if the object changes in position
    protected ArrayList<Drawable> dependencies = new ArrayList<Drawable>();


    // ADD DEFAULT CONSTRUCTOR WITH rETTER FUNCTIONS

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
    // This function handles all logic of how to draw the component in differing situations.
    public void drawImage(Graphics2D g2d, int originx, int originy) {
        if (isSelected) {
            drawSelected(g2d, originx, originy);
        } else {
            drawNotSelected(g2d, originx, originy);
        }
    }

    protected abstract void drawSelected(Graphics2D g, int originx, int originy);

    protected abstract void drawNotSelected(Graphics2D g, int originx, int originy);



    // Mouse Interaction variables
    //      Returns true if a click with absolute coords (offsets from origin) x and y should count as a click
    //      TODO: Refactor so it short circuits on invisibility, and then calls another abstract function to check click
    //              this is because this fn is potentially computationally expensive
    //      TODO: Make new abstract function inHitbox(tolerance), and call it from here, so I don't forget tolerance.
    //              That's proper robust.
    public abstract boolean inHitbox(int x, int y);

    public void toggleSelected() {
        isSelected = !isSelected;
    }

    // Gets the type of the component, as specified in model
    public abstract String getType();

    // Drag variables
    private boolean isBeingDragged = false;
    private int dragStartX;
    private int dragStartY;
    public abstract boolean isMoveable();

    // Update position relative to plane origin
    public void addOffset(int offsetX, int offsetY) {
        this.coordX += offsetX;
        this.coordY += offsetY;
    }

    // Set absolute position of the object
    // In screen coords
    public void setOffset(int offsetX, int offsetY) {
        this.coordX = offsetX;
        this.coordY = offsetY;
    }

    // Add space to the bounding box
    public void addBounding(int dx, int dy) {
        this.boundingX = dx;
        this.boundingY = dy;
    }

    // List of everything which must be recomputed if this component moves
    public ArrayList<Drawable> getDependencies() {
        return dependencies;
    }

    public void addDependency(Drawable d) {
        dependencies.add(d);
    }

    // Recalculates position
    public abstract void recompute();


    // Useful coord functions
    // TODO: oroginx, originy virtual to screen

}
