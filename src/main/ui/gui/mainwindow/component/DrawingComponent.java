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
import java.util.List;

public class DrawingComponent extends JPanel implements MouseListener {

    public static final Dimension MIN_SIZE = new Dimension(300, 100);

    private List<Drawable> components;

    private Graphics2D g2;

    // Represents the coordinate of the top left pixel
    private int coordsx;
    private int coordsy;

    // Background Image
    private BufferedImage backgroundImage;
    private int backgroundWidth;
    private int backgroundHeight;

    // Dragging
    private boolean planeIsDragging = false;
    private int dragBeginx;
    private int dragBeginy;


    // TODO: Fix component drawing (or rather, lack thereof) for resizes!
    public DrawingComponent() {
        // Init coordinate system
        coordsx = 0;
        coordsy = 0;
        components = new ArrayList<Drawable>();

        // Init background
        try {
            backgroundImage = ImageIO.read(new File("./res/210Background2.png"));
        } catch (IOException e) {
            // Crash that
            throw new UncheckedIOException(e);
        }
        backgroundWidth = backgroundImage.getWidth();
        backgroundHeight = backgroundImage.getHeight();

        // This is just example code for now. Usually, components is populated by load operations or button
        ExampleDrawableCircle circ = new ExampleDrawableCircle();
        circ.setOffset(100, 100);
        components.add(circ);

        // Final inits
        addMouseListener(this);
    }

    // This is run every time the screen is drawn
    @Override
    public void paintComponent(Graphics g) {
        // Add border so you aren't drawing right on the edge?
        this.g2 = (Graphics2D) g;
        super.paintComponent(g2);

        // Each update needs to redraw the background and any drawable components
        displayBackground();
        displayDrawable();

        // Test to track (0,0)
        g2.drawLine(0,0, coordsx, coordsy);
        g2.drawRect(0,0, drawingWidth(), drawingHeight());

    }


    // Displays background to screen
    private void displayBackground() {
        int totalXTiles = drawingWidth() / backgroundWidth + 1;
        int totalYTiles = drawingHeight() / backgroundHeight + 1;

        // "Moving" the background is identical mod the tile size
        int subtileXOffset = Math.floorMod(coordsx, backgroundWidth);
        int subtileYOffset = Math.floorMod(coordsy, backgroundHeight);

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


    // Displays all Drawable object iff it is in the screen
    private void displayDrawable() {
        for (Drawable d : components) {
            if (d.getToDraw()) {
                d.drawImage(g2, coordsx, coordsy);
            }
        }
    }

    // Updates the drawability of all components (used for like, entire screen moves)
    private void updateToDraw() {
        for (Drawable d : components) {
            d.updateToDraw(-coordsx, drawingWidth() - coordsx, -coordsy, drawingHeight() - coordsy);
        }
    }


    // Forces update of entire scene and redraws everything
    public void updateAndRedrawAll() {
        displayBackground();
        updateToDraw();
        displayDrawable();
    }



    // EFFECTS: Returns the x co-ordinate of the bottom right pixel
    private int drawingWidth() {
        return Math.round((float)getSize().getWidth()) - 1;
    }

    // EFFECTS: Returns the x co-ordinate of the bottom right pixel
    private int drawingHeight() {
        return Math.round((float)getSize().getHeight()) - 1;
    }


    public void addOffset(int offsetx, int offsety) {
        coordsx += offsetx;
        coordsy += offsety;
    }

    public void setOffset(int offsetx, int offsety) {
        coordsx = offsetx;
        coordsy = offsety;
    }


    // MOUSE LISTENER
    @Override
    public void mouseClicked(MouseEvent e) {
        // Nothing
    }

    // Extract to draggable interface, for all components
    @Override
    public void mousePressed(MouseEvent e) {
        planeIsDragging = true;
        dragBeginx = e.getXOnScreen();
        dragBeginy = e.getYOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing if the click began outside of the screen
        if (planeIsDragging) {
            planeIsDragging = false;
            // Drag plane
            dragPlane(e.getXOnScreen() - dragBeginx,
                    e.getYOnScreen() - dragBeginy);
            repaint();
        }
    }

    // These functions grab the edges of the screen
    private int getLeft() {
        return -coordsx;
    }

    private int getRight() {
        return drawingWidth() - coordsx;
    }

    private int getTop() {
        return -coordsy;
    }

    private int getBot() {
        return drawingHeight() - coordsy;
    }


    // When plane is dragged, we must update the visibility of all objects.
    private void dragPlane(int dx, int dy) {
        addOffset(dx, dy);
        updateToDraw();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Nothing
    }
}
