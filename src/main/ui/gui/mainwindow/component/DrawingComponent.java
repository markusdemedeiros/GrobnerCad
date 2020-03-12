package ui.gui.mainwindow.component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DrawingComponent extends JPanel implements MouseListener {
    public static final Dimension MIN_SIZE = new Dimension(300, 100);

    private Graphics2D g2;

    // List of all components on screen
    //      Order determines component selection (change this in data pannels?)
    private List<Drawable> components;
    private List<Drawable> selected;

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
    private boolean planeIsDragging = false;
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
        circ2.setOffset(250, 500);
        components.add(circ2);
        GraphicalLine gl = new GraphicalLine(circ, circ2);
        components.add(gl);

        // Final JComponent initialization
        addMouseListener(this);
    }


    // This is run every time the screen is drawn
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
        selected = new ArrayList<Drawable>();
    }


    // Extract to draggable interface(?), for all components
    @Override
    public void mousePressed(MouseEvent e) {
        planeIsDragging = true;
        dragBeginX = e.getXOnScreen();
        dragBeginY = e.getYOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing if the click began outside of the screen
        if (planeIsDragging) {
            planeIsDragging = false;
            // Drag plane
            dragPlane(e.getXOnScreen() - dragBeginX,
                    e.getYOnScreen() - dragBeginY);
            repaint();
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


}
