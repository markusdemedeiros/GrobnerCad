package model;

import model.algebraic.Constraint;
import model.geometric.Geometry;

import java.util.ArrayList;
import java.util.List;

// Class to represent a complete system of algebraic and geometric objects
//      Right now it's just used to save/load, but I should really refactor the solver/analyst
//      to take fullsystems for better cohesion
public class FullSystem {
    private List<Geometry> geometery;
    private List<Constraint> algebra;

    // MODIFIES: this
    public FullSystem(List<Geometry> geometery, List<Constraint> algebra) {
        this.geometery = geometery;
        this.algebra = algebra;
    }


}
