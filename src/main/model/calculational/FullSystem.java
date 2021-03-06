package model.calculational;

import model.algebraic.Constraint;
import model.geometric.Geometry;
import model.persistence.Saveable;

import java.io.PrintWriter;
import java.util.List;

// Class to represent a complete system of algebraic and geometric objects
//      Right now it's just used to save/load, but I should really refactor the solver/analyst
//      to take fullsystems for better cohesion
public class FullSystem implements Saveable {
    private List<Geometry> geometery;
    private List<Constraint> algebra;

    // EFFECTS: Constructor
    // MODIFIES: this
    public FullSystem(List<Geometry> geometery, List<Constraint> algebra) {
        this.geometery = geometery;
        this.algebra = algebra;
    }

    // Getter
    public List<Geometry> getGeometery() {
        return geometery;
    }

    // Getter
    public List<Constraint> getAlgebra() {
        return algebra;
    }

    public Constraint[] getAlgebraAsArray() {
        Constraint[] output = new Constraint[algebra.size()];
        for (int i = 0; i < algebra.size(); i++) {
            output[i] = algebra.get(i);
        }
        return output;
    }

    @Override
    public void save(PrintWriter printWriter) {
        printWriter.println(getStringToPrint());
    }

    public String getStringToPrint() {
        String output = "";
        output += "{GEO}\n";
        for (Geometry geo : geometery) {
            output += geo.show() + "\n";
        }
        output += "{ALG}\n";
        for (Constraint con : algebra) {
            output += con.show() + "\n";
        }
        return output;
    }

}
