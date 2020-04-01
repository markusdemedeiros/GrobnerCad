package ui.gui.mainwindow.component;

import model.algebraic.Constraint;
import model.calculational.FullSystem;
import model.geometric.Geometry;
import model.geometric.Point;
import ui.DataGUI;
import ui.gui.mainwindow.component.linelabels.ConstraintDistanceLabel;
import ui.gui.mainwindow.component.linelabels.ConstraintHorizontalLineLabel;
import ui.gui.mainwindow.component.linelabels.ConstraintVerticalLineLabel;
import ui.gui.mainwindow.component.pointlabels.ConstraintSetXLabel;
import ui.gui.mainwindow.component.pointlabels.ConstraintSetYLabel;
import ui.gui.mainwindow.exceptions.IncorrectSelectionException;
import ui.gui.mainwindow.graphicpersistence.GraphicInfo;
import ui.gui.mainwindow.graphicpersistence.LineGraphicInfo;
import ui.gui.mainwindow.graphicpersistence.PointGraphicInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


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
    private GlobalCoordinateSystem gcs;

    // Background image variables
    private BufferedImage backgroundImage;
    private int backgroundWidth;
    private int backgroundHeight;

    // Dragging flags
    //      validDrag is true iff the drag begins in the component
    //      dragBeginx and dragBeginy are measured in screen coordinates (relative to component)
    private boolean validDrag;
    private int dragBeginX;
    private int dragBeginY;



    // TODO: Fix component drawing (or rather, lack thereof) for resizes and component init
    public DrawingComponent() {
        // Setup variables to blank slate
        blankInit();

        // Background init
        try {
            backgroundImage = ImageIO.read(DataGUI.backgroundImageFile);
        } catch (IOException e) {
            // Programming error
            throw new UncheckedIOException(e);
        }
        backgroundWidth = backgroundImage.getWidth();
        backgroundHeight = backgroundImage.getHeight();

        // JComponent init (final operations)
        addMouseListener(this);
        recomputeAll();
    }


    // MODIFIES: this
    // EFFECTS: Removes EVERYTHING!!!! Can be called to clear the screen.
    public void blankInit() {
        gcs = new GlobalCoordinateSystem(this);

        validDrag = false;

        // Drawable component list init
        components = new ArrayList<>();
        selected = new LinkedList<>();

    }

    public void loadComponents(ArrayList<Drawable> components) {
        this.components = components;
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
        /*
        int topleftx = voriginX - DataGUI.ORIGIN_SIZE;
        int toplefty = voriginY - DataGUI.ORIGIN_SIZE;
        int botrightx = voriginX + DataGUI.ORIGIN_SIZE;
        int botrighty = voriginY + DataGUI.ORIGIN_SIZE;
         */

        int topleftx = gcs.liftToScreenX(-DataGUI.ORIGIN_SIZE);
        int toplefty = gcs.liftToScreenY(-DataGUI.ORIGIN_SIZE);
        int botrightx = gcs.liftToScreenX(DataGUI.ORIGIN_SIZE);
        int botrighty = gcs.liftToScreenY(DataGUI.ORIGIN_SIZE);

        g2.drawLine(topleftx, toplefty, botrightx, botrighty);
        g2.drawLine(topleftx, botrighty, botrightx, toplefty);
    }


    // EFFETCS: Draws a box on the edge of the drawable region
    // MODIFIES: this
    private void drawBoundingBox() {
        g2.drawRect(0,0, gcs.screenWidth() - 1, gcs.screenHeight() - 1);
    }


    // EFFECTS: Draws the stored background image to the screen
    // MODIFIES: this
    // REQUIRES: backgroundImage is not an empty image
    private void displayBackground() {
        // TODO: Experiment with different tile sizes. What makes Graphics2D the fastest?
        // TODO: Experiment with different drawing methods. Maybe drawImage is not optimal. Furthermore,
        //          experiment with non-default 'observer' fields
        // TODO: These two variables only need to be updated on screen resize, extract to listener?
        int totalXTiles = gcs.screenWidth() / backgroundWidth + 1;
        int totalYTiles = gcs.screenHeight() / backgroundHeight + 1;

        // "Moving" the background is mathematically identical mod the tile size
        int subtileXOffset = Math.floorMod(gcs.getVirtualX(), backgroundWidth);
        int subtileYOffset = Math.floorMod(gcs.getVirtualY(), backgroundHeight);

        for (int i = 0; i <= totalYTiles; i++) {
            for (int j = 0; j <= totalXTiles; j++) {
                int x = (j - 1) * backgroundWidth + subtileXOffset;
                int y = (i - 1) * backgroundHeight + subtileYOffset;
                g2.drawImage(backgroundImage, x, y, null);
            }
        }
    }


    /*
    // EFFECTS: adds a Drawable component to the list with local origin (originX, originY)
    //          and repaints component (I want there never to be a situation where a component is added and not shown)
    // MODIFIES: this, d
    public void addDrawable(Drawable d, int originX, int originY) {
        d.setOffset(originX, originY);
        components.add(d);
        updateToDraw();
        repaint();
    }

     */


    // EFFECTS: Displays drawable component to the screen iff it has toDraw set.
    // MODIFIES: this
    private void displayDrawable() {
        for (Drawable d : components) {
            if (d.getToDraw()) {
                d.drawImage(g2, gcs);
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
        d.updateToDraw(gcs);
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
        gcs.shiftCoordinates(dx, dy);
        updateToDraw();
    }


    // EFFECTS: Generates a list of all objects at the position (x,y) in virtual coordinates
    private List<Drawable> getAllObjectsAtPosition(int x, int y) {
        List<Drawable> output = new ArrayList<>();
        for (Drawable c : components) {
            if (c.inHitbox(x, y)) {
                output.add(c);
            }
        }
        return output;
    }



    /*
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
    */

    // =================================================================================================================
    // MOUSE EVENT HANDLING

    // EFFECTS: handles mouse click
    // MODIFIES: this
    @Override
    public void mouseClicked(MouseEvent e) {
        doClick(e);
    }

    // EFFECTS: On a mouse click
    //              * If there is no object underneath mouse, deselect all items
    //              * If there is one object underneath mouse, toggle selection of that item
    //              * If there are many objects underneath the screen, prompt user for single item via popup
    //                      and toggle that item
    //              * Then repaint the screen
    // MODIFIES: this, objects in selected and components
    private void doClick(MouseEvent e) {
        // If there is one or zero clicked then proceed with behavior immediately
        //List<Drawable> clicked = getAllObjectsAtPosition(screenToOriginX(e.getX()),
        //        screenToOriginY(e.getY()));

        List<Drawable> clicked = getAllObjectsAtPosition(gcs.pushToVirtualX(e.getX()), gcs.pushToVirtualY(e.getY()));

        if (clicked.size() == 0) {
            deselectAll();
            repaint();
        } else if (clicked.size() == 1) {
            processClickOnItem(clicked.get(0));
            repaint();
        } else {
            JPopupMenu popup = createItemSelector(clicked);
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    // EFFECTS: Creates a popup menu for the user to select a single item from a list of candidate items
    //              When the user selects an item, it will process a click on that item and repaint the screen
    // MODIFIES: this
    private JPopupMenu createItemSelector(List<Drawable> candidates) {
        JPopupMenu popup = new JPopupMenu();
        for (Drawable d : candidates) {
            JMenuItem menuItem = new JMenuItem(d.getType());
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    processClickOnItem(d);
                    repaint();
                }
            });
            popup.add(menuItem);
        }
        return popup;
    }

    // EFFECTS: Toggles the selectable of the clicked item, and adds or removes it to local list of selected objects
    // MODIFIES: this
    private void processClickOnItem(Drawable clicked) {
        if (selected.contains(clicked)) {
            clicked.toggleSelected();
            selected.remove(clicked);
        } else {
            clicked.toggleSelected();
            selected.add(clicked);
        }
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
                boolean startedOnSelected = false;
                for (Drawable d : getAllObjectsAtPosition(gcs.pushToVirtualX(dragBeginX),
                        gcs.pushToVirtualY(dragBeginY))) {
                    startedOnSelected = startedOnSelected || selected.contains(d);
                }
                if (!startedOnSelected) {
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

    // Creates new point in the center of the screen and redraws
    public void createNewPoint() {
        GraphicalPoint output = new GraphicalPoint();
        output.addOffset(gcs.pushToVirtualX(gcs.screenWidth() / 2),
                gcs.pushToVirtualY(gcs.screenHeight() / 2));
        createNewDrawable(output);
        repaint();
    }


    // EFFECTS: Adds a new drawable to the list of drawables and updates it's visibility
    // MODIFIES: this
    private void createNewDrawable(Drawable d) {
        d.updateToDraw(gcs);
        components.add(d);
    }

    // EFFECTS: Creates new line form selected components if those components can create a line
    //              That is, they are the two endpoints (for now)
    //          If no line can be created from selected, throw IncorrectSelectionException with message to user.
    // MODIFIES: this
    public void createNewLineFromSelected() throws IncorrectSelectionException {
        List<GraphicalPoint> points = isTwoPointsSelected();
        createNewDrawable(new GraphicalLine(points.get(0), points.get(1)));
        repaint();
    }

    // EFFECTS: Creates new horizontal constraint from selected components if it is exactly one line
    //              and if it doesn't already have a horizontal constraint
    //              throws incorrectSelectionException with message otherwise
    //          repaints when done
    // MODIFIES: this
    public void createNewHorizFromSelected() throws IncorrectSelectionException {
        GraphicalLine line = isOneLineSelected();
        checkDrawableForLabelType(line, Constraint.PP_HORIZONTAL_TYPE);
        createNewDrawable(new ConstraintHorizontalLineLabel(line));
        repaint();
    }

    // EFFECTS: Creates new vertical constraint from selected components if it is exactly one line
    //              and if it doesn't already have a vertical constraint
    //              throws incorrectSelectionException with message otherwise
    //          repaints when done
    // MODIFIES: this
    public void createNewVertFromSelected() throws IncorrectSelectionException {
        GraphicalLine line = isOneLineSelected();
        checkDrawableForLabelType(line, Constraint.PP_VERTICAL_TYPE);
        createNewDrawable(new ConstraintVerticalLineLabel(line));
        repaint();
    }

    // EFFECTS: Creates new distance constraint from selected components if it is exactly one line
    //              and if it doesn't already have a distance constraint
    //              throws incorrectSelectionException with message otherwise
    //          repaints when done
    // MODIFIES: this
    public void createNewDistFromSelected(double dist) throws IncorrectSelectionException {
        GraphicalLine line = isOneLineSelected();
        checkDrawableForLabelType(line, Constraint.PP_DISTANCE_TYPE);
        createNewDrawable(new ConstraintDistanceLabel(line, dist));
        repaint();
    }

    // EFFECTS: Creates new setx constraint from selected components if it is exactly one point
    //              and if it doesn't already have a setx constraint
    //              throws incorrectSelectionException with message otherwise
    //          repaints when done
    // MODIFIES: this
    public void createNewSetXFromSelected(double dist) throws IncorrectSelectionException {
        GraphicalPoint point = isOnePointSelected();
        checkDrawableForLabelType(point, Constraint.P_SETX_CONSTRAINT);
        createNewDrawable(new ConstraintSetXLabel(point, dist));
        repaint();
    }

    // EFFECTS: Creates new sety constraint from selected components if it is exactly one point
    //              and if it doesn't already have a sety constraint
    //              throws incorrectSelectionException with message otherwise
    //          repaints when done
    // MODIFIES: this
    public void createNewSetYFromSelected(double dist) throws IncorrectSelectionException {
        GraphicalPoint point = isOnePointSelected();
        checkDrawableForLabelType(point, Constraint.P_SETY_CONSTRAINT);
        createNewDrawable(new ConstraintSetYLabel(point, dist));
        repaint();
    }

    public void createNewCoincidentFromSelected() throws IncorrectSelectionException {
        List<GraphicalPoint> points = isTwoPointsSelected();
        for (Drawable d : points.get(0).getDependencies()) {
            if (d.getType() == Constraint.PP_COINCIDENT_TYPE
                    && d.getDependencies().contains(points.get(1))) {
                // If p0 has a dependency which is a PP Coincident constraint which also has p1 as a dependency,
                //      the two points are coincident by definition. No dupicates.
                throw new IncorrectSelectionException("Those two points are already coincident");
            }
        }
        createNewDrawable(new ConstraintCoincidentLabel(points.get(0), points.get(1)));
        repaint();
    }


    // EFFECTS: Returns a list of exactly two graphicalpoints, if there are exactly two GraphicaPoints selected
    //              throws an exception with instructions to the user if not
    private List<GraphicalPoint> isTwoPointsSelected() throws IncorrectSelectionException {
        if ((selected.size() != 2)
                || (selected.get(0).getType() != Geometry.TYPE_POINT)
                || (selected.get(1).getType() != Geometry.TYPE_POINT)) {
            throw new IncorrectSelectionException("Please select two points");
        } else {
            List<GraphicalPoint> output = new ArrayList<>();
            output.add((GraphicalPoint) selected.get(0));
            output.add((GraphicalPoint) selected.get(1));
            return output;
        }
    }

    // EFFECTS: Returns a GraphicalPoint if exactly one point is selected
    //              throws an exception with instructions to the user if not
    private GraphicalPoint isOnePointSelected() throws IncorrectSelectionException {
        if ((selected.size() != 1)
                || (selected.get(0).getType() != Geometry.TYPE_POINT)) {
            throw new IncorrectSelectionException("Please select one point");
        } else {
            return (GraphicalPoint) selected.get(0);
        }
    }

    // EFFECTS: Returns a GraphicalLine exactly one line is selected
    //              throws exception with user instructions otherwise
    private GraphicalLine isOneLineSelected() throws IncorrectSelectionException {
        if ((selected.size() != 1)
                || (selected.get(0).getType() != Geometry.TYPE_LINE)) {
            throw new IncorrectSelectionException("Please select one line");
        } else {
            return (GraphicalLine) selected.get(0);
        }
    }

    // EFFECTS: throws incorrectselectionexception if gl has a label of type type
    private void checkDrawableForLabelType(Drawable gl, String type) throws IncorrectSelectionException {
        for (Drawable d : gl.getDependencies()) {
            if (d.getType() == type) {
                throw new IncorrectSelectionException("That object already has that type of constraint");
            }
        }
    }

    // EFFECTS: throws incorrect

    @Override
    public void mouseEntered(MouseEvent e) {
        // Nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Nothing
    }

    // =================================================================================================================
    // GRABBING DATA AS OTHER DATA FORMATS

    public String getAsString() {
        HashMap<Drawable, String> generatedNames = new HashMap<>();
        ArrayList<Geometry> geoElements = new ArrayList<>();
        // First, take care of the points
        // It will be helpful to have a dict bewteen the two so I don't have to write a seach function
        HashMap<Drawable, Point> pointDict = new HashMap<>();
        for (Drawable d : components) {
            if (d.getType().equals(Geometry.TYPE_POINT)) {
                String name = "P" + geoElements.size(); // Ensures name is unique to each point
                generatedNames.put(d, name);
                Point p = new Point(name);
                pointDict.put(d, p);
                geoElements.add(p);
            }
        }
        // That's all the points put in GeoElements
        // Now to add the constraints. Each has a createPure function, which creates a pure (algebraic) constraint
        // given just a list of points and$aame. This makes it pretty straightforward.
        ArrayList<Constraint> conElements = generateConElements(pointDict);
        FullSystem fs = new FullSystem(geoElements, conElements);
        ArrayList<GraphicInfo> graphics = generateGraphics(generatedNames);

        String output = generateFileString(fs, graphics);
        return output;
    }

    private String generateFileString(FullSystem fs, ArrayList<GraphicInfo> graphics) {
        String output = "";
        output += "{GFX}\n";
        for (GraphicInfo g : graphics) {
            output += g.writeAsString() + "\n";
        }
        output += fs.getStringToPrint();
        return output;
    }

    private ArrayList<Constraint> generateConElements(HashMap<Drawable, Point> pointDict) {
        ArrayList<Constraint> conElements = new ArrayList<>();
        for (Drawable d : components) {
            String type = d.getType();
            if (Constraint.TYPES.contains(type)) {
                conElements.add(((DrawableConstraint) d).makePure(pointDict, "C" + components.size()));
            }
        }
        return conElements;
    }

    private ArrayList<GraphicInfo> generateGraphics(HashMap<Drawable, String> generatedNames) {
        ArrayList<GraphicInfo> graphics = new ArrayList<>();
        for (Drawable d : components) {
            if (d.getType().equals(Geometry.TYPE_POINT)) {
                graphics.add(new PointGraphicInfo(generatedNames.get(d), d.coordX, d.coordY));
            } else if (d.getType().equals(Geometry.TYPE_LINE)) {
                String name = "L" + graphics.size();
                graphics.add(new LineGraphicInfo(name,
                        generatedNames.get(((GraphicalLine) d).getP1()),
                        generatedNames.get(((GraphicalLine) d).getP2())));
            }
        }
        return graphics;
    }

}
