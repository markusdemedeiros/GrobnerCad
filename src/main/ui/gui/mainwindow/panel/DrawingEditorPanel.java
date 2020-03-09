package ui.gui.mainwindow.panel;

import ui.DataGUI;
import ui.gui.mainwindow.component.DrawingComponent;

import javax.swing.*;
import java.awt.*;

public class DrawingEditorPanel extends JPanel {
    private DrawingComponent dc;
    private JToolBar jt;
    private JPanel drawing; // Contains DrawingComponent and any related static components. Uses a gridBagLayout.



    public DrawingEditorPanel() {
        // Layout is BorderLayout with drawing in the center. This is to enable JToolbar to snap to any edge
        setLayout(new BorderLayout());

        // Toolbar
        // TODO: Mulitline toolbar
        // TODO: Toggle construction
        // TODO: Extract to function
        jt = new JToolBar(JToolBar.VERTICAL);
        jt.setBackground(DataGUI.DRK_GREY);

        jt.add(makeToolbarButton(DataGUI.TEST_ICON));
        jt.add(makeToolbarButton(DataGUI.TEST_ICON2));
        jt.addSeparator();
        jt.add(makeToolbarButton(DataGUI.TEST_ICON3));
        jt.add(makeToolbarButton(DataGUI.TEST_ICON4));


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

    // Makes a toolbar button
    private JButton makeToolbarButton(Icon label) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(DataGUI.TOOLBAR_BUTTON_SIZE, DataGUI.TOOLBAR_BUTTON_SIZE));
        button.setBorderPainted(false);
        return button;
    }

    // These functions can be called by MainWindow- they really only refer to what other parts of the program can do
    //          to the pannel

    public void redrawAll() {
        dc.updateAndRedrawAll();
    }



}