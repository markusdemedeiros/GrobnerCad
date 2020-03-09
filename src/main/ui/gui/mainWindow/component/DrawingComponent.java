package ui.gui.mainWindow.component;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class DrawingComponent extends JPanel {

    public static final Dimension MIN_SIZE = new Dimension(300, 100);

    private Graphics2D g2;

    @Override
    public void paintComponent(Graphics g) {
        g2 = (Graphics2D) g;
        super.paintComponent(g2);
        g2.setBackground(Color.red);
        Dimension size = getSize();
        g2.drawRect(0, 0, drawingWidth(), drawingHeight());
    }


    // EFFECTS: Returns the x co-ordinate of the bottom right pixel
    private int drawingWidth() {
        return Math.round((float)getSize().getWidth()) - 1;
    }

    // EFFECTS: Returns the x co-ordinate of the bottom right pixel
    private int drawingHeight() {
        return Math.round((float)getSize().getHeight()) - 1;
    }



}
