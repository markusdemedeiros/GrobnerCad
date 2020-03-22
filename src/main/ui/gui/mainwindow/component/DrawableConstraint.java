package ui.gui.mainwindow.component;

import model.algebraic.Constraint;
import model.geometric.Point;

import java.util.HashMap;

public interface DrawableConstraint {
    public Constraint makePure(HashMap<Drawable, Point> pointDict, String name);

}
