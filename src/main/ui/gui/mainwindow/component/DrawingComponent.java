package ui.gui.mainwindow.component;

import model.exceptions.IncorrectSelectionException;
import ui.DataGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Thread.sleep;


// THE LITTLE 2D DRAWING ENGINE THAT COULD
public class DrawingComponent extends JPanel implements MouseListener {
    public static final Dimension MIN_SIZE = new Dimension(300, 100);

    private Graphics2D g2;

    // List of all components on screen
    //      Order determines component selection (change this in data pannels?)
    private List<Drawable> components;
    private ArrayList<Drawable> selected;

    // Screen co-ordinates of virtual origin
    private int voriginx;
    private int voriginy;

    // Background Image
    private BufferedImage backgroundImage;
    private int backgroundWidth;
    private int backgroundHeight;

    // Dragging flags
    //      dragBeginx and dragBeginy are measured in absolute co-ordinates (relative to monitor)
    //      this allows drags to exit the component
    private boolean validDrag = false;  // True iff drag begins in component
    private int dragBeginX;
    private int dragBeginY;


    // TODO: Fix component drawing (or rather, lack thereof) for resizes!
    public DrawingComponent() {
        // Initialize coordinate system
        voriginx = 0;
        voriginy = 0;

        components = new ArrayList<Drawable>();
        selected = new ArrayList<Drawable>();

        // Init background
        try {
            backgroundImage = ImageIO.read(new File("./res/210Background3.png"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        backgroundWidth = backgroundImage.getWidth();
        backgroundHeight = backgroundImage.getHeight();

        // This is just example code for now. Usually, components is populated by load operations or button
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

        // Final JComponent initialization
        addMouseListener(this);
    }


    // This is run every time the screen is drawn
    // TODO: Look into docs, paintcomponet might not be the best place to but this (bug at init).
    @Override
    public void paintComponent(Graphics g) {
        this.g2 = (Graphics2D) g;
        super.paintComponent(g2);

        // Each update needs to redraw the background and any drawable components
        // TODO: Does every update really need to update the background? Not a huge compute sink for now.
        displayBackground();
        displayDrawable();

        // Test to track (0,0)
        int originsize = 5;
        g2.drawLine(voriginx - originsize, voriginy - originsize, voriginx + originsize, voriginy + originsize);
        g2.drawLine(voriginx - originsize, voriginy + originsize, voriginx + originsize, voriginy - originsize);

        // Bounding rectangle
        g2.drawRect(0,0, drawingWidth(), drawingHeight());

    }


    // Displays background to screen
    private void displayBackground() {
        // TODO: Experiment with different tile sizes. What makes Graphics2D the fastest?
        // TODO: Experiment with different drawing methods. Maybe drawImage is not optimal.
        // TODO: These two variables only need to be updated on screen resize, extract to listener.
        int totalXTiles = drawingWidth() / backgroundWidth + 1;
        int totalYTiles = drawingHeight() / backgroundHeight + 1;

        // "Moving" the background is identical mod the tile size
        int subtileXOffset = Math.floorMod(voriginx, backgroundWidth);
        int subtileYOffset = Math.floorMod(voriginy, backgroundHeight);

        for (int i = 0; i <= totalYTiles; i++) {
            for (int j = 0; j <= totalXTiles; j++) {
                int x = (j - 1) * backgroundWidth + subtileXOffset;
                int y = (i - 1) * backgroundHeight + subtileYOffset;
                g2.drawImage(backgroundImage,
                        x,
                        y,
                        null);  // TODO: Look into observer.
            }
        }
    }



    // Adds a component to the screen and redraws
    public void addDrawable(Drawable d, int localOriginX, int localOriginY) {
        d.setOffset(localOriginX, localOriginY);
        components.add(d);
        updateToDraw();
        repaint();
    }


    // EFFECTS: Displays drawable component to the screen iff it has toDraw set.
    private void displayDrawable() {
        for (Drawable d : components) {
            if (d.getToDraw()) {
                d.drawImage(g2, voriginx, voriginy);
            }
        }
    }

    // EFFECTS: Forces an update of toDraw on all components, over the whole screen. Used in screen moves.
    private void updateToDraw() {
        for (Drawable d : components) {
            d.updateToDraw(getLft(), getRgt(), getTop(), getBot());
        }
    }

    // EFFECTS: Forces toDraw check on all components, and then redraws entire screen including background.
    public void updateAndRedrawAll() {
        updateToDraw();
        displayBackground();
        displayDrawable();
    }

    // EFFECTS: Adds (dx, dy) to virtual origin and updated the toDraw of all objects.
    private void dragPlane(int dx, int dy) {
        addOffset(dx, dy);
        updateToDraw();
    }


    // Gets all objects at position
    private List<Drawable> getAllObjectsAtPosition(int x, int y) {
        return null;
    }

    // Gets top object at position
    private Drawable getObjectAtPosition(int x, int y) {
        for (Drawable c : components) {
            if (c.inHitbox(x, y)) {
                return c;
            }
        }
        return null;
    }



    // ===========================================================
    // MOUSE EVENT HANDLING

    // Mouse selects items and repaints
    // I'm a little worried this has too much coupling. There might be a better way to do this in another class.
    @Override
    public void mouseClicked(MouseEvent e) {
        doClick(e);
    }

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

    private void deselectAll() {
        for (Drawable d : selected) {
            d.toggleSelected();
        }
        this.selected = new ArrayList<Drawable>();
        repaint();
    }


    // Extract to draggable interface(?), for all components
    @Override
    public void mousePressed(MouseEvent e) {
        validDrag = true;
        dragBeginX = e.getX();
        dragBeginY = e.getY();
    }

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
                if (selected.size() == 0
                || !selected.contains(getObjectAtPosition(screenToOriginX(dragBeginX), screenToOriginY(dragBeginY)))) {
                    dragPlane(dx, dy);
                } else {
                    // Move all selected points
                    moveSelected(dx, dy);
                }
                repaint();
            }
        }
    }

    // Recomputes at a list of dirty points, and averything effected by them
    // ** ACK! INFINITE LOOPS BE HERE **
    // Possible to Break them with an ischanged function
    //      However, that does not include asymptotic phenomena.
    //      Must write a termination proof for the types of moves I am doing (should be pretty easy, it's linear)

    // PROOF SKETCH:
    //      A move depends only on the object's intrinsic qualitites, so can be determined on an object-level.
    //      The process of determining all dirty elements terminates as a list of at most every element
    //      Hence, a move is equivalent to finding all dirty elements and then individually moving them
    //          and the other object dependencies are well defined no matter the order of the move
    //          provided they are computed after the move takes place.
    public void moveSelected(int dx, int dy) {
        // We only want to move the moveable objects!
        for (Drawable d : selected) {
            if (d.isMoveable()) {
                d.addOffset(dx, dy);
            }
        }

        // Then after all moves, recompute the attributes of all objects effected by the move
        for (Drawable d : getDirty((ArrayList<Drawable>) selected.clone())) {
            d.recompute();
        }
    }

    // Set up the recursion
    private ArrayList<Drawable> getDirty(ArrayList<Drawable> partialDirty) {
        return dirtyRecursion(partialDirty, new ArrayList<Drawable>());
    }

    // Recursively walks dependency tree
    private ArrayList<Drawable> dirtyRecursion(ArrayList<Drawable> toCompute, ArrayList<Drawable> computed) {
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

    @Override
    public void mouseEntered(MouseEvent e) {
        // Nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Nothing
    }


    // ===========================================================
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
        return screenX - voriginx;
    }

    // EFFECTS: Converts screen y coordinate to origin y coordinate
    private int screenToOriginY(int screenY) {
        return screenY - voriginy;
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
    public void addOffset(int offsetX, int offsetY) {
        voriginx += offsetX;
        voriginy += offsetY;
    }

    // EFFECTS: Changes the position of the virtual origin to (offsetX, offsetY)
    public void setOffset(int offsetX, int offsetY) {
        voriginx = offsetX;
        voriginy = offsetY;
    }



    // Just makes a new line from two points
    public void createNewLine(GraphicalPoint p1, GraphicalPoint p2) {
        GraphicalLine output = new GraphicalLine(p1, p2);
        output.updateToDraw(getLft(), getRgt(), getTop(), getBot());
        components.add(output);
    }


    // Creates new line and repaints
    public void createNewLineFromSelected() throws IncorrectSelectionException {
        if (selected.size() != 2 ||
        selected.get(0).getType() != Drawable.POINT_ID ||
        selected.get(1).getType() != Drawable.POINT_ID) {
            throw new IncorrectSelectionException("Please select two points");
        } else {
            createNewLine((GraphicalPoint) selected.get(0),
                    (GraphicalPoint) selected.get(1));
        }
        repaint();
    }


}
