package model.geometric;

import model.geometric.Geometry;

// Geometric point represented as a pair of coordinates (x,y)
public class Point extends Geometry {

    // MODIFIES: this
    public Point(String name) {
        this.type = TYPE_POINT;
        this.numVariables = VARS_POINT;
        this.name = name;
    }

    // EFFECTS: Generates string co-responding to x coordinate variable
    public String xvar() {
        return getVariable(1);
    }

    // EFFECTS: Generates string co-responding to y coordinate variable
    public String yvar() {
        return getVariable(2);
    }

    @Override
    public String showHuman() {
        return show();
    }
}
