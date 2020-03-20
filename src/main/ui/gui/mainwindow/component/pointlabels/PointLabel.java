package ui.gui.mainwindow.component.pointlabels;

import model.geometric.Point;
import ui.DataGUI;
import ui.gui.mainwindow.component.Drawable;
import ui.gui.mainwindow.component.GraphicalPoint;

import javax.xml.crypto.Data;

public abstract class PointLabel extends Drawable {

    protected GraphicalPoint point;
    protected PointPosition position;

    public PointLabel(GraphicalPoint point, PointPosition position) {
        super();
        this.point = point;
        this.position = position;
        this.addDependency(this.point);
        this.point.addDependency(this);
        recompute();
    }

    @Override
    public boolean isMoveable() {
        return false;
    }



    protected abstract int positionalOffsetY();
    protected abstract int positionalOffsetX();


    @Override
    public void recompute() {
        this.coordX = point.getVirtualCenterX() + positionalOffsetX();
        this.coordY = point.getVirtualCenterY() + positionalOffsetY();
    }
}
