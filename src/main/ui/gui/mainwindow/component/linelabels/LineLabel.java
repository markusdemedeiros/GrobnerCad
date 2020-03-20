package ui.gui.mainwindow.component.linelabels;

import ui.gui.mainwindow.component.Drawable;
import ui.gui.mainwindow.component.GraphicalLine;

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

    protected abstract boolean isAbove();


}
