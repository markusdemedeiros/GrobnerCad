package ui.gui.mainwindow.component;

import model.exceptions.ZeroSlopeException;

import java.awt.*;

// Represents a label on a line
public abstract class LineLabel extends Drawable {

    protected GraphicalLine line;

    public LineLabel(GraphicalLine line) {
        super();
        this.line = line;
        this.addDependency(this.line);
        this.line.addDependency(this);
    }

    @Override
    public boolean isMoveable() {
        return false;
    }


}
