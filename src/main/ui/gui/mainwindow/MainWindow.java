package ui.gui.mainwindow;

import com.formdev.flatlaf.*;
import com.sun.corba.se.impl.orbutil.graph.Graph;
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


// Handles layout of components for main drawing window
// Uses flat Darcula LAF

public class MainWindow extends JFrame {

    private FullSystem currentSystem;

    public static final Dimension MIN_SIZE = new Dimension(700, 500);
    public static final Dimension DEF_SIZE = new Dimension(1000, 600);
    private DrawingEditorPanel dep;

    // Adds components to main UI
    @SuppressWarnings("checkstyle:MethodLength")
    private void initUI() {
        setLayout(new GridBagLayout());
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        this.setJMenuBar(menubar());

        // Drawing editor
        dep = new DrawingEditorPanel();
        add(dep, new GridBagConstraints(0, 0, 1, 1,
                1, 1,
                GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        /*  Constraint panels are not used in this build.
                I need to focus on implementing all the functionality before providing alternate ways to use it :S
        // Panels
        DataPanel cp = new DataPanel(currentSystem);
        add(cp, new GridBagConstraints(1, 0, 1, 1,
                0, 1,
                GridBagConstraints.LINE_END, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        */

        // ================================
        // WINDOW PARAMATERS
        setTitle("Drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(MIN_SIZE);
        setSize(DEF_SIZE);
    }


    // Returns a JMenubar for the GUI Menu
    private JMenuBar menubar() {
        JMenuBar menubar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem writeItem = new JMenuItem("Save");
        JMenuItem readItem = new JMenuItem("Open");
        JMenuItem quitItem = new JMenuItem("Quit");

        JMenu graphicMenu = new JMenu("Graphics");
        JMenuItem repaintItem = new JMenuItem("Repaint");
        JMenuItem recomputeItem = new JMenuItem("Recompute All");


        fileMenu.add(writeItem);
        fileMenu.add(readItem);
        fileMenu.add(quitItem);

        graphicMenu.add(repaintItem);
        graphicMenu.add(recomputeItem);

        menubar.add(fileMenu);
        menubar.add(graphicMenu);

        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Default close operation from JFrame attached to MainWindow
                MainWindow.super.dispose();
            }
        });

        repaintItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Do nothing else for now
                dep.redrawAll();
            }
        });

        recomputeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dep.recomputeAll();
            }
        });

        readItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = JOptionPane.showInputDialog("Which system would you like to load?");
                if (filename != null) {
                    File toRead = new File ("./data/" + filename + ".sys");
                    try {
                        loadSystem(toRead);
                    } catch (IOException ex) {
                        miscIOErrorDisplay();
                    }
                }
            }
        });

        return menubar;
    }

    // The load system is kinda (okay super) hacky, but I need a functional product to test other parts of the system
    private void loadSystem(File toRead) throws IOException {
        dep.clearScreen();
        FullSystem fs = Reader.readSystem(toRead);
        try {
            List<GraphicInfo> gfx = Reader.readGraphics(toRead);
            HashMap<String, Drawable> elementsToLoad = new HashMap<String, Drawable>();

            // First we load in the points
            for (GraphicInfo g : gfx) {
                if (g.type.equals(Geometry.TYPE_POINT)) {
                    int ox = ((PointGraphicInfo) g).getCoordx();
                    int oy = ((PointGraphicInfo) g).getCoordy();
                    GraphicalPoint output = new GraphicalPoint();
                    output.setOffset(ox, oy);
                    elementsToLoad.put(g.name, output);
                }
            }
            // Now we have all the information to add the lines
            for (GraphicInfo g : gfx) {
                if (!g.type.equals(Geometry.TYPE_POINT)) {
                    // It works but it hurts
                    elementsToLoad.put(g.name,
                            new GraphicalLine((GraphicalPoint) elementsToLoad.get(((LineGraphicInfo) g).getp0()),
                                    (GraphicalPoint) elementsToLoad.get(((LineGraphicInfo) g).getp1())));
                }
            }
            // Now we have the information to load in the constraints
            // Welcome to hell. This URGENTLY needs to be made not-temporary with a big boy type hierarchy
            for (Constraint c : fs.getAlgebra()) {
                if (c.getType().equals(Constraint.P_SETY_CONSTRAINT)) {
                    GraphicalPoint pt = (GraphicalPoint) elementsToLoad.get(((PSetYConstraint) c).getPoint().getName());
                    double yv = ((PSetYConstraint) c).getYval();
                    elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                            new ConstraintSetYLabel(pt, yv));
                } else if (c.getType().equals(Constraint.P_SETX_CONSTRAINT)) {
                    GraphicalPoint pt = (GraphicalPoint) elementsToLoad.get(((PSetXConstraint) c).getPoint().getName());
                    double xv = ((PSetXConstraint) c).getxval();
                    elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                            new ConstraintSetXLabel(pt, xv));
                } else if (c.getType().equals(Constraint.PP_DISTANCE_TYPE)) {
                    GraphicalPoint p1 = (GraphicalPoint) elementsToLoad.get(((PPDistanceConstraint) c).getP1().getName());
                    GraphicalPoint p2 = (GraphicalPoint) elementsToLoad.get(((PPDistanceConstraint) c).getP2().getName());
                    double dist = ((PPDistanceConstraint) c).getDistance();
                    GraphicalLine ln = linefromPointsInList(p1, p2, elementsToLoad.values());
                    elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                            new ConstraintDistanceLabel(ln, dist));
                } else if (c.getType().equals(Constraint.PP_COINCIDENT_TYPE)) {
                    GraphicalPoint p1 = (GraphicalPoint) elementsToLoad.get(((PPCoincidentConstraint) c).getP1().getName());
                    GraphicalPoint p2 = (GraphicalPoint) elementsToLoad.get(((PPCoincidentConstraint) c).getP2().getName());
                    elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                            new ConstraintCoincidentLabel(p1, p2));
                } else if (c.getType().equals(Constraint.PP_HORIZONTAL_TYPE)) {
                    GraphicalPoint p1 = (GraphicalPoint) elementsToLoad.get(((PPHorizontalConstraint) c).getP1().getName());
                    GraphicalPoint p2 = (GraphicalPoint) elementsToLoad.get(((PPHorizontalConstraint) c).getP2().getName());
                    GraphicalLine ln = linefromPointsInList(p1, p2, elementsToLoad.values());
                    elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                            new ConstraintHorizontalLineLabel(ln));
                } else if (c.getType().equals(Constraint.PP_VERTICAL_TYPE)) {
                    GraphicalPoint p1 = (GraphicalPoint) elementsToLoad.get(((PPVerticalConstraint) c).getP1().getName());
                    GraphicalPoint p2 = (GraphicalPoint) elementsToLoad.get(((PPVerticalConstraint) c).getP2().getName());
                    GraphicalLine ln = linefromPointsInList(p1, p2, elementsToLoad.values());
                    elementsToLoad.put(Integer.toString(elementsToLoad.size()),
                            new ConstraintVerticalLineLabel(ln));
                } else {
                    // Programming error, for now
                }
            }

            // All the graphics are now loaded :)
            dep.loadSystem(new ArrayList<Drawable>(elementsToLoad.values()));

        } catch (NoGraphicsException e) {
            // TODO: loading of old or CLI generated systems by having placement subroutine
            noGFXDisplay();
        }
    }

    private GraphicalLine linefromPointsInList(GraphicalPoint p0, GraphicalPoint p1, Collection<Drawable> toDraw) {
        for (Drawable d : toDraw) {
            if ((d.getType().equals(Geometry.TYPE_LINE)) && (((GraphicalLine)d).getEndpoints().contains(p0)) && (((GraphicalLine)d).getEndpoints().contains(p1))) {
                return ((GraphicalLine) d);
            }
        }

        return null;
    }


    private void noGFXDisplay() {
        JFrame f = new JFrame("No graphics info");
        JOptionPane.showMessageDialog(f, "Selected file has no graphic info, placement not implemented. Aborting.");
        f.dispose();
    }

    private void miscIOErrorDisplay() {
        JFrame f = new JFrame("File error");
        JOptionPane.showMessageDialog(f, "A filesystem error has been encountered. Aborting.");
        f.dispose();
    }

    public MainWindow() {
        try {
            currentSystem = Reader.readSystem(new File("./data/allconstraints.sys"));
        } catch (IOException e) {
            // Just crash for now, I will handle this later
            throw new UncheckedIOException(e);
        }

        initUI();
    }
}


