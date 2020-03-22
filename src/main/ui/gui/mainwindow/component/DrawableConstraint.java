package ui.gui.mainwindow.component;

import model.algebraic.Constraint;
import model.geometric.Point;

import java.util.HashMap;
import java.util.List;

public interface DrawableConstraint {
    public Constraint makePure(HashMap<Drawable, Point> pointDict, String name);

}
