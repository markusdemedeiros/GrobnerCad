package ui.gui.mainWindow.panel;

import sun.java2d.loops.GeneralRenderer;
import ui.gui.mainWindow.MainWindow;
import ui.gui.mainWindow.component.DrawingComponent;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DrawingEditorPanel extends JPanel {
    private DrawingComponent dc;
    private JToolBar jt;
    private JPanel drawing; // Contains DrawingComponent and any related static components. Uses a gridBagLayout.


    public DrawingEditorPanel() {
        // Layout is BorderLayout with drawing in the center. This is to enable JToolbar to snap to any edge
        setLayout(new BorderLayout());

        // TODO: Remove this. If I see blue it means an error.
        setBackground(Color.BLUE);
        setOpaque(true);

        // Toolbar
        jt = new JToolBar(JToolBar.VERTICAL);
        jt.setBackground(MainWindow.DRK_GREY);
        JButton jb1 = new JButton("A");
        jt.add(jb1);
        JButton jb2 = new JButton("B");
        jt.add(jb2);

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



}
