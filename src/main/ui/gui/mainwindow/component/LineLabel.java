package ui.gui.mainwindow.component;

import java.awt.*;

// Represents a label on a line
public abstract class LineLabel extends Drawable {

    protected GraphicalLine line;
    protected int midX;
    protected int midY;

    public LineLabel(GraphicalLine line) {
        super();
        this.line = line;
        this.addDependency(this.line);
    }

    @Override
    public boolean inHitbox(int x, int y) {
        return false;
    }


    @Override
    public boolean isMoveable() {
        return false;
    }

    @Override
    public void recompute() {
        // Happens automatically on redraw
    }
}
