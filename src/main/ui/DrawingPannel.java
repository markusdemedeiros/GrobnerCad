package ui;

import javax.swing.*;
import java.awt.*;

// Have I depricated this class? I think so...
public class DrawingPannel extends JPanel {

    public DrawingPannel() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLUE);
        g2d.drawLine(0,0,800,500);
    }
}
