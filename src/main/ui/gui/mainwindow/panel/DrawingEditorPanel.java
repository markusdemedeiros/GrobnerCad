package ui.gui.mainwindow.panel;

import model.exceptions.IncorrectSelectionException;
import ui.DataGUI;
import ui.gui.mainwindow.component.DrawingComponent;
import ui.gui.mainwindow.component.GraphicalPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrawingEditorPanel extends JPanel {
    private DrawingComponent dc;
    private JToolBar jt;
    private JPanel drawing; // Contains DrawingComponent and any related static components. Uses a gridBagLayout.

    // TODO: Add graphical border so you aren't drawing right on the edge? Perhaps in DrawingPannel.


    public DrawingEditorPanel() {
        // Layout is BorderLayout with drawing in the center. This is to enable JToolbar to snap to any edge
        setLayout(new BorderLayout());

        // Toolbar
        // TODO: Mulitline toolbar
        // TODO: Toggle construction mode
        // TODO: Extract to function
        jt = new JToolBar(JToolBar.VERTICAL);
        jt.setBackground(DataGUI.DRK_GREY);

        // ADD GEOMETRY BUTTONS
        jt.add(makeAddPointButton());
        jt.add(makeAddLineButton());
        jt.addSeparator();

        // LINE-BASED CONSTRAINT BUTTONS
        JButton coinc = makeToolbarButton(DataGUI.PPCOINC_ICON);
        jt.add(coinc);
        JButton distance = makeToolbarButton(DataGUI.PPDST_ICON);
        jt.add(distance);
        JButton horiz = makeToolbarButton(DataGUI.PPHORIZ_ICON);
        jt.add(horiz);
        JButton vert = makeToolbarButton(DataGUI.PPVERT_ICON);
        jt.add(vert);
        jt.addSeparator();

        // POINT BASED CONSTRAINT BUTTONS
        JButton setx = makeToolbarButton(DataGUI.PSETX_ICON);
        jt.add(setx);
        JButton sety = makeToolbarButton(DataGUI.PSETY_ICON);
        jt.add(sety);


        // Drawing Panel
        drawing = new JPanel();
        dc = new DrawingComponent();
        drawing.setLayout(new GridBagLayout());
        drawing.add(dc, new GridBagConstraints(0, 0, 1, 1,
                1.0, 1.0,
                GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
                new Insets(8, 8, 8, 8), 0, 0));

        // Add components
        add(drawing, BorderLayout.CENTER);
        add(jt, BorderLayout.WEST);
    }

    // =================================================================================================================
    // TOOLBAR GENERATION FUNCTIONS

    // EFFECTS: Returns a new toolbar button
    private JButton makeToolbarButton(Icon label) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(DataGUI.TOOLBAR_BUTTON_SIZE, DataGUI.TOOLBAR_BUTTON_SIZE));
        button.setBorderPainted(false);
        return button;
    }

    // EFFECTS: returns new addPoint button
    private JButton makeAddPointButton() {
        JButton addPoint = makeToolbarButton(DataGUI.ADDPOINT_ICON);
        addPoint.addActionListener(new ActionListener() {
            // Places point at (50, 50) for testing purposes
            @Override
            public void actionPerformed(ActionEvent e) {
                dc.addDrawable(new GraphicalPoint(), 50, 50);
            }
        });
        return addPoint;
    }

    // EFFECTS: returns new addLine button
    private JButton makeAddLineButton() {
        JButton newLineButton = makeToolbarButton(DataGUI.ADDLINE_ICON);
        newLineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dc.createNewLineFromSelected();
                } catch (IncorrectSelectionException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        return newLineButton;
    }

    // =================================================================================================================
    // MISC FUNCTIONS
    // These functions can be called by MainWindow- they really only refer to what other parts of the program can do
    //          to the pannel

    // MODIFIES: dc
    // EFFECTS: updates and redraws all drawn elements
    public void redrawAll() {
        dc.updateAndRedrawAll();
    }

    // MODIFIES: dc
    // EFFECTS: recomputes all graphical elements
    public void recomputeAll() {
        dc.recomputeAll();
    }




}
