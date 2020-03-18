package ui.gui.mainwindow.component;

import model.exceptions.IncorrectSelectionException;
import ui.DataGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;


// Jpanel which handles the display and graphical editing of a drawing
//      "The little 2D graphics engine that could"
public class DrawingComponent extends JPanel implements MouseListener {

    // Local graphics2d object which all methods draw to.
    // This is updated every redraw
    private Graphics2D g2;

    // List of all components on screen ordered by height
    //      Elements with lower index in the list are selected before elements with higher index
    //      I've chosen Arraylist because of it's speed in search
    private ArrayList<Drawable> components;

    // List of selected elements, sublist of components
    //      I've chosen LinkedList because of it's speed in insertion and removal
    //      NOTE: The order of this list is not of relevance.
    private LinkedList<Drawable> selected;

    // Position of the virtual origin in screen coordinates.
    private int voriginX;
    private int voriginY;

    // Background image variables
    private BufferedImage backgroundImage;
    private int backgroundWidth;
    private int backgroundHeight;

    // Dragging flags
    //      validDrag is true iff the drag begins in the component
    //      dragBeginx and dragBeginy are measured in screen coordinates (relative to component)
    private boolean validDrag = false;
    private int dragBeginX;
    private int dragBeginY;


    // TODO: Fix component drawing (or rather, lack thereof) for resizes and component init
    public DrawingComponent() {
        // Coordinate systems init
        voriginX = 0;
        voriginY = 0;

        // Drawable component list init
        components = new ArrayList<>();
        selected = new LinkedList<>();

        // Background init
        try {
            backgroundImage = ImageIO.read(DataGUI.backgroundImageFile);
        } catch (IOException e) {
            // Programming error
            throw new UncheckedIOException(e);
        }
        backgroundWidth = backgroundImage.getWidth();
        backgroundHeight = backgroundImage.getHeight();

        // This is just example code for now. Usually, components is populated by load operations or button
        setupTestComponents();

        // JComponent init (final operations)
        addMouseListener(this);
        recomputeAll();
    }


    // EFFECTS: Puts some test geometry to the screen.
    // MODIFIES: this
    private void setupTestComponents() {
        GraphicalPoint circ = new GraphicalPoint();
        circ.setOffset(80, 220);
        components.add(circ);
        GraphicalPoint circ2 = new GraphicalPoint();
        circ2.setOffset(250, 0);
        components.add(circ2);
        GraphicalPoint circ3 = new GraphicalPoint();
        circ3.setOffset(300, 100);
        components.add(circ3);
        GraphicalPoint circ4 = new GraphicalPoint();
        circ4.setOffset(10, 10);
        components.add(circ4);
        GraphicalLine gl = new GraphicalLine(circ, circ2);
        components.add(gl);
        GraphicalLine gl2 = new GraphicalLine(circ2, circ3);
        components.add(gl2);
        GraphicalLine gl4 = new GraphicalLine(circ, circ3);
        components.add(gl4);
        GraphicalLine gl3 = new GraphicalLine(circ, circ4);
        components.add(gl3);
        ConstraintHorizontalLineLabel ll1 = new ConstraintHorizontalLineLabel(gl3, 0.3);
        components.add(ll1);
    }


    // TODO: Look into docs, paintcomponet might not be the best place to but this (bug at init)
    // NOTE: this method is called every time repaint() is called
    // EFFECTS: Draws all of the components to the screen as specified
    // MODIFIES: this
    @Override
    public void paintComponent(Graphics g) {
        this.g2 = (Graphics2D) g;
        super.paintComponent(g2);
        displayBackground();
        displayDrawable();
        drawOrigin();
        drawBoundingBox();
    }


    // EFFECTS: Draws a shape which tracks the origin to the screen
    // MODIFIES: this
    private void drawOrigin() {
        g2.drawLine(voriginX - DataGUI.ORIGIN_SIZE,
                voriginY - DataGUI.ORIGIN_SIZE,
                voriginX + DataGUI.ORIGIN_SIZE,
                voriginY + DataGUI.ORIGIN_SIZE);
        g2.drawLine(voriginX - DataGUI.ORIGIN_SIZE,
                voriginY + DataGUI.ORIGIN_SIZE,
                voriginX + DataGUI.ORIGIN_SIZE,
                voriginY - DataGUI.ORIGIN_SIZE);
    }


    // EFFETCS: Draws a box on the edge of the drawable region
    // MODIFIES: this
    private void drawBoundingBox() {
        g2.drawRect(0,0, drawingWidth(), drawingHeight());
    }


    // EFFECTS: Draws the stored background image to the screen
    // MODIFIES: this
    // REQUIRES: backgroundImage is not an empty image
    private void displayBackground() {
        // TODO: Experiment with different tile sizes. What makes Graphics2D the fastest?
        // TODO: Experiment with different drawing methods. Maybe drawImage is not optimal. Furthermore,
        //          experiment with non-default 'observer' fields
        // TODO: These two variables only need to be updated on screen resize, extract to listener?
        int totalXTiles = drawingWidth() / backgroundWidth + 1;
        int totalYTiles = drawingHeight() / backgroundHeight + 1;

        // "Moving" the background is mathematically identical mod the tile size
        int subtileXOffset = Math.floorMod(voriginX, backgroundWidth);
        int subtileYOffset = Math.floorMod(voriginY, backgroundHeight);

        for (int i = 0; i <= totalYTiles; i++) {
            for (int j = 0; j <= totalXTiles; j++) {
                int x = (j - 1) * backgroundWidth + subtileXOffset;
                int y = (i - 1) * backgroundHeight + subtileYOffset;
                g2.drawImage(backgroundImage,
                        x,
                        y,
                        null);
            }
        }
    }

    
    // EFFECTS: adds a Drawable component to the list with local origin (originX, originY)
    //          and repaints component (I want there never to be a situation where a component is added and not shown)
    // MODIFIES: this, d
    public void addDrawable(Drawable d, int originX, int originY) {
        d.setOffset(originX, originY);
        components.add(d);
        updateToDraw();
        repaint();
    }


    // EFFECTS: Displays drawable component to the screen iff it has toDraw set.
    // MODIFIES: this
    private void displayDrawable() {
        for (Drawable d : components) {
            if (d.getToDraw()) {
                d.drawImage(g2, voriginX, voriginY);
            }
        }
    }

    // EFFECTS: Forces an update of toDraw on all components
    //              Typical use case: operations such as screen moves, which potentially changes the
    //              visibility of all components
    // MODIFIES: this, every Drawable in components
    private void updateToDraw() {
        for (Drawable d : components) {
            updateToDrawEntireScreen(d);
        }
    }


    // EFFECTS: Updates the toDraw of a drawable, considering only it's position on the screen
    //              (not, for example, considering interference with other components)
    // MODIFIES: this, d
    private void updateToDrawEntireScreen(Drawable d) {
        d.updateToDraw(getLft(), getRgt(), getTop(), getBot());
    }

    // EFFECTS: Forces toDraw check on all components, and then redraws entire screen including background.
    // MODIFIES: this, every Drawable in components
    public void updateAndRedrawAll() {
        updateToDraw();
        displayBackground();
        displayDrawable();
    }

    // EFFECTS: Adds (dx, dy) to virtual origin
    //          Updates toDraw of every object (never want a situation where plane moves and visibility is not updated)
    // MODIFIES: this, every Drawable in components
    private void dragPlane(int dx, int dy) {
        addOffset(dx, dy);
        updateToDraw();
    }


    // EFFECTS: Generates a list of all objects at the position (x,y) in virtual coordinates
    private List<Drawable> getAllObjectsAtPosition(int x, int y) {
        return null;
    }


    // EFFECTS: Gets the top object at the position (x,y) in virtual coordinates,
    //              or null if no object at the position
    private Drawable getObjectAtPosition(int x, int y) {
        for (Drawable c : components) {
            if (c.inHitbox(x, y)) {
                return c;
            }
        }
        return null;
    }

    // =================================================================================================================
    // MOUSE EVENT HANDLING

    // EFFECTS: handles mouse click
    // MODIFIES: this
    @Override
    public void mouseClicked(MouseEvent e) {
        doClick(e);
    }

    // EFFECTS: On a mouse click
    //              * toggle the selection of an item which is clicked on
    //              * deselect all items if background is clicked on
    //              * repaint the screen
    // MODIFIES: this, objects in selected and components
    private void doClick(MouseEvent e) {
        Drawable clicked = getObjectAtPosition(screenToOriginX(e.getX()),
                screenToOriginY(e.getY()));

        if (clicked == null) {
            deselectAll();
        } else if (selected.contains(clicked)) {
            clicked.toggleSelected();
            selected.remove(clicked);
        } else {
            clicked.toggleSelected();
            selected.add(clicked);
        }
        repaint();
    }

    // EFFECTS: Toggles all selected back to deselected, resets the list of selected items, and repaints screen
    // MODIFIES: this
    private void deselectAll() {
        for (Drawable d : selected) {
            d.toggleSelected();
        }
        this.selected = new LinkedList<Drawable>();
        repaint();
    }


    // EFFECTS: Logs a valid mouse press as having started in the component, and records it's co-ordinates
    // MODIFIES: this
    @Override
    public void mousePressed(MouseEvent e) {
        validDrag = true;
        dragBeginX = e.getX();
        dragBeginY = e.getY();
    }

    // EFFECTS: If the mouse is released after a a drag starting in the component, and travelled more than
    //              DataGUI.CLICK_SENS_TOLERANCE in both x and y, then drag and repaint either
    //                  * the plane, if the drag did not begin on a selected component or
    //                  * the selected components otherwise
    //          If the mouse drag did begin in the component, register the drag as being completed but
    //                  do not trigger any further action (otherwise duplicate clicks can be detected)
    // MODIFIES: this
    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing if the click began outside of the screen
        if (validDrag) {
            validDrag = false;
            int dx = e.getX() - dragBeginX;
            int dy = e.getY() - dragBeginY;
            // Don't do zero length drags, a zero length drag should be registered as a click
            if (Math.abs(dx) > DataGUI.CLICK_SENS_TOLERANCE && Math.abs(dy) > DataGUI.CLICK_SENS_TOLERANCE) {
                // True drag has been detected. Move the items if the drag started on a selected object
                if ((selected.size() == 0) || !selected.contains(getObjectAtPosition(screenToOriginX(dragBeginX),
                        screenToOriginY(dragBeginY)))) {
                    dragPlane(dx, dy);
                } else {
                    // Move all selected points
                    moveSelected(dx, dy);
                }
                repaint();
            }
        }
    }


    // EFFETCS: Moves the selected components which are moveabvle by (dx, dy) pixels, recalculates the scene, and
    //              repaints everything.
    // MODIFIES: this
    // NOTE: Since motion of components is a function in the mathematical sense (it depends only on a the position
    //              of each individual component), this is equivalent to moving all valid pieces seperately, and then
    //              recomputing the scene.
    public void moveSelected(int dx, int dy) {
        LinkedList<Drawable> moved = new LinkedList<>();
        // We only want to move the moveable objects!
        for (Drawable d : selected) {
            if (d.isMoveable()) {
                moved.add(d);
                d.addOffset(dx, dy);
            }
        }

        // Then after all moves, recompute the attributes of all objects effected by the move
        // NOTE: we must get the dirty elements ONLY for those which have actually been moved, NOT all selected
        for (Drawable d : getDirty(moved)) {
            d.recompute();
        }
    }

    // Force coodinate recomputation on all elements
    public void recomputeAll() {
        for (Drawable d : components) {
            d.recompute();
        }
    }

    // EFFECTS: Returns a list of components that will need to be recomputed if the list partiallyDirty is moved
    private ArrayList<Drawable> getDirty(List<Drawable> partialDirty) {
        return dirtyRecursion(partialDirty, new ArrayList<Drawable>());
    }

    // EFFECTS: Recursively walks the dependency tree of onbjects in toCompute and generates a list of
    //              dependencies for those items.
    // NOTE: Designed to be called by getDirty
    private ArrayList<Drawable> dirtyRecursion(List<Drawable> toCompute, ArrayList<Drawable> computed) {
        if (toCompute.size() == 0) {
            return computed;
        } else {
            // Remove the first element of the list and mark it as computed
            Drawable head = toCompute.get(0);
            toCompute.remove(0);
            computed.add(head);

            // Get everything the head depends upon
            ArrayList<Drawable> headDeps = head.getDependencies();
            // We must compute all of headDeps not in toCompute and not in computed.
            for (Drawable d : headDeps) {
                if (!toCompute.contains(d) && !computed.contains(d)) {
                    toCompute.add(d);
                }
            }

            // Then calculate the remainder
            return dirtyRecursion(toCompute, computed);
        }
    }

    // =================================================================================================================
    // OBJECT ADDITION

    // EFFETCS: Creates a new line from two points, and adds it to components
    // MODIFIES: this
    public void createNewLine(GraphicalPoint p1, GraphicalPoint p2) {
        GraphicalLine output = new GraphicalLine(p1, p2);
        output.updateToDraw(getLft(), getRgt(), getTop(), getBot());
        components.add(output);
    }


    // EFFECTS: Creates new line form selected components if those components can create a line
    //              That is, they are the two endpoints (for now)
    //          If no line can be created from selected, throw IncorrectSelectionException with message to user.
    // MODIFIES: this
    public void createNewLineFromSelected() throws IncorrectSelectionException {
        if ((selected.size() != 2)
                || (selected.get(0).getType() != Drawable.POINT_ID)
                || (selected.get(1).getType() != Drawable.POINT_ID)) {
            throw new IncorrectSelectionException("Please select two points");
        } else {
            createNewLine((GraphicalPoint) selected.get(0),
                    (GraphicalPoint) selected.get(1));
        }
        repaint();
    }


    @Override
    public void mouseEntered(MouseEvent e) {
        // Nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Nothing
    }


    // =================================================================================================================
    // COORDINATE TRANSFORMS AND OPERATIONS THEREOF

    // * Screen co-ords are the literal pixels displayed to screen
    //  (0,0) ---------------- (drawingWidth, 0)
    //   |                      |
    //  (drawingHeight, 0) ----(drawingWidth, drawingHeight)

    private int drawingWidth() {
        return (int) (getSize().getWidth() - 1);
    }

    private int drawingHeight() {
        return (int) (getSize().getHeight() - 1);
    }

    //  * Origin co-ords are coordinates relative to virtual origin
    //          The virtual origin is located at (voriginx, voriginy)
    //  (getLft(), getTop()) ------------------------- (getRgt(), getTop())
    //   |                                              |
    //   |                                              |
    //   |          (0, 0)                              |
    //   |                                              |
    //   (getLft(), getBot() ------------------------- (getRgt(), getBot())


    // EFFECTS: Converts screen x coordinate to origin x coordinate
    private int screenToOriginX(int screenX) {
        return screenX - voriginX;
    }

    // EFFECTS: Converts screen y coordinate to origin y coordinate
    private int screenToOriginY(int screenY) {
        return screenY - voriginY;
    }

    private int getLft() {
        return screenToOriginX(0);
    }

    private int getRgt() {
        return screenToOriginX(drawingWidth());
    }

    private int getTop() {
        return screenToOriginY(0);
    }

    private int getBot() {
        return screenToOriginY(drawingHeight());
    }

    // EFFECTS: Offsets the virtual origin by (offsetX, offsetY)
    // MODIFIES: this
    public void addOffset(int offsetX, int offsetY) {
        voriginX += offsetX;
        voriginY += offsetY;
    }

    // EFFECTS: Changes the position of the virtual origin to (offsetX, offsetY)
    // MODIFIES: this
    public void setOffset(int offsetX, int offsetY) {
        voriginX = offsetX;
        voriginY = offsetY;
    }






}
