package ui.gui.mainwindow.graphicalPersistence;

import model.algebraic.*;
import model.calculational.FullSystem;
import model.geometric.Geometry;
import model.persistence.Reader;
import ui.gui.mainwindow.component.ConstraintCoincidentLabel;
import ui.gui.mainwindow.component.Drawable;
import ui.gui.mainwindow.component.GraphicalLine;
import ui.gui.mainwindow.component.GraphicalPoint;
import ui.gui.mainwindow.component.linelabels.ConstraintDistanceLabel;
import ui.gui.mainwindow.component.linelabels.ConstraintHorizontalLineLabel;
import ui.gui.mainwindow.component.linelabels.ConstraintVerticalLineLabel;
import ui.gui.mainwindow.component.pointlabels.ConstraintSetXLabel;
import ui.gui.mainwindow.component.pointlabels.ConstraintSetYLabel;
import ui.gui.mainwindow.exceptions.NoGraphicsException;
import ui.gui.mainwindow.panel.DrawingEditorPanel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


// Class to facilitate loading graphical information into a drawingEditor
// NOTE: Graphical information is required ONLY for the GUI saving and loading. All the solving features of this program
//          operate on fullsystems, which have no lines or positional information for points. That graphical information
//          only exists so humans can interact with it, and hence why I am putting this class in UI rather than
//          in model. The model does not need to know how the humans see the graphics! That's the beauty of using
//          the Groebner Basis / Algebraic Geometry approach! :)
public abstract class GraphicalLoader {

    // EFFECTS: Reads the graphical, geometric, and algebraic information in a .sys file, generates a list of
    //              drawable elements, and loads them into the DrawingEditorPanel to be displayed to the user.
    //          Throws IOException if the file cannot be read
    //          Throws NoGraphicsException if the .sys file contains no graphical information
    // MODIFIES: dep
    // REQUIRES: toRead is a valid .sys file
    public static void loadIntoDrawingEditor(File toRead, DrawingEditorPanel dep)
            throws IOException, NoGraphicsException {
        List<GraphicInfo> gfx = Reader.readGraphics(toRead);
        FullSystem fs = Reader.readSystem(toRead);
        HashMap<String, Drawable> elementsToLoad = new HashMap<String, Drawable>();

        // First we load in the points
        loadPoints(gfx, elementsToLoad);

        // Now we have all the information to add the lines
        loadNonPoints(gfx, elementsToLoad);

        // Now we have the information to load in the constraints
        // Welcome to hell. Add this to the lis of stuff to refactor in PHASE 4- the cohesion is TERRIBLE
        loadAlgebra(elementsToLoad, fs.getAlgebra());

        // All the graphics are now loaded :)
        dep.loadSystem(new ArrayList(elementsToLoad.values()));
    }

    // EFFECTS: Generates GraphicalPoints for each PointGraphicInfo
    private static void  loadPoints(List<GraphicInfo> gfx, HashMap<String, Drawable> elementsToLoad) {
        for (GraphicInfo g : gfx) {
            if (g.type.equals(Geometry.TYPE_POINT)) {
                elementsToLoad.put(g.name, ((PointGraphicInfo) g).generate());
            }
        }
    }

    private static void loadNonPoints(List<GraphicInfo> gfx, HashMap<String, Drawable> elementsToLoad) {
        for (GraphicInfo g : gfx) {
            if (!g.type.equals(Geometry.TYPE_POINT)) {
                // It works but it hurts. Refactor for PHASE 4
                elementsToLoad.put(g.name,
                        new GraphicalLine((GraphicalPoint) elementsToLoad.get(((LineGraphicInfo) g).getp0()),
                                (GraphicalPoint) elementsToLoad.get(((LineGraphicInfo) g).getp1())));
            }
        }
    }


    private static void loadAlgebra(HashMap<String, Drawable> elementsToLoad, List<Constraint> constraints) {
        for (Constraint c : constraints) {
            if (c.getType().equals(Constraint.P_SETY_CONSTRAINT)) {
                loadSetY(elementsToLoad, (PSetYConstraint) c);
            } else if (c.getType().equals(Constraint.P_SETX_CONSTRAINT)) {
                loadSetX(elementsToLoad, (PSetXConstraint) c);
            } else if (c.getType().equals(Constraint.PP_DISTANCE_TYPE)) {
                loadDistance(elementsToLoad, (PPDistanceConstraint) c);
            } else if (c.getType().equals(Constraint.PP_COINCIDENT_TYPE)) {
                loadCoincident(elementsToLoad, (PPCoincidentConstraint) c);
            } else if (c.getType().equals(Constraint.PP_HORIZONTAL_TYPE)) {
                loadHorizontal(elementsToLoad, (PPHorizontalConstraint) c);
            } else if (c.getType().equals(Constraint.PP_VERTICAL_TYPE)) {
                loadVertical(elementsToLoad, (PPVerticalConstraint) c);
            }
        }
    }

    private static void loadVertical(HashMap<String, Drawable> elementsToLoad, PPVerticalConstraint c) {
        GraphicalPoint p1 = (GraphicalPoint) elementsToLoad.get(c.getP1().getName());
        GraphicalPoint p2 = (GraphicalPoint) elementsToLoad.get(c.getP2().getName());
        GraphicalLine ln = linefromPointsInList(p1, p2, elementsToLoad.values());
        elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                new ConstraintVerticalLineLabel(ln));
    }

    private static void loadHorizontal(HashMap<String, Drawable> elementsToLoad, PPHorizontalConstraint c) {
        GraphicalPoint p1 = (GraphicalPoint) elementsToLoad.get(c.getP1().getName());
        GraphicalPoint p2 = (GraphicalPoint) elementsToLoad.get(c.getP2().getName());
        GraphicalLine ln = linefromPointsInList(p1, p2, elementsToLoad.values());
        elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                new ConstraintHorizontalLineLabel(ln));
    }

    private static void loadCoincident(HashMap<String, Drawable> elementsToLoad, PPCoincidentConstraint c) {
        GraphicalPoint p1 = (GraphicalPoint) elementsToLoad.get(c.getP1().getName());
        GraphicalPoint p2 = (GraphicalPoint) elementsToLoad.get(c.getP2().getName());
        elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                new ConstraintCoincidentLabel(p1, p2));
    }

    private static void loadDistance(HashMap<String, Drawable> elementsToLoad, PPDistanceConstraint c) {
        GraphicalPoint p1 = (GraphicalPoint) elementsToLoad.get(c.getP1().getName());
        GraphicalPoint p2 = (GraphicalPoint) elementsToLoad.get(c.getP2().getName());
        double dist = c.getDistance();
        GraphicalLine ln = linefromPointsInList(p1, p2, elementsToLoad.values());
        elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                new ConstraintDistanceLabel(ln, dist));
    }

    private static void loadSetY(HashMap<String, Drawable> elementsToLoad, PSetYConstraint c) {
        GraphicalPoint pt = (GraphicalPoint) elementsToLoad.get(c.getPoint().getName());
        double yv = c.getYval();
        elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                new ConstraintSetYLabel(pt, yv));
    }

    private static void loadSetX(HashMap<String, Drawable> elementsToLoad, PSetXConstraint c) {
        GraphicalPoint pt = (GraphicalPoint) elementsToLoad.get(c.getPoint().getName());
        double xv = c.getxval();
        elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                new ConstraintSetXLabel(pt, xv));
    }

    private static GraphicalLine linefromPointsInList(GraphicalPoint p0,
                                                      GraphicalPoint p1,
                                                      Collection<Drawable> toDraw) {
        for (Drawable d : toDraw) {
            if ((d.getType().equals(Geometry.TYPE_LINE))
                    && (((GraphicalLine)d).getEndpoints().contains(p0))
                    && (((GraphicalLine)d).getEndpoints().contains(p1))) {
                return ((GraphicalLine) d);
            }
        }

        return null;
    }
}
