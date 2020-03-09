package ui.gui.mainWindow.panel;

import javax.swing.*;
import java.awt.*;

public class DataPanel extends JPanel {

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    public DataPanel() {
        JScrollPane scrollpane = new JScrollPane();
        String[] data = {"Item 4", "Item 5", "Item 6"};
        JList<String> jl = new JList<String>(data);
        scrollpane.setViewportView(jl);
        scrollpane.setSize(getPreferredSize());
        add(scrollpane);
        add(scrollpane);
    }

}
