package model.algebraic;

import model.calculational.PolynomialForm;
import model.geometric.Point;

import java.util.ArrayList;

// Constraint to fix the y-coordinate of a point
public class PSetYConstraint extends Constraint {
    Point p0;   // The point
    double yval;

    public PSetYConstraint(String name, Point p0, double yval) {
        this.type = P_SETY_CONSTRAINT;
        this.name = name;
        this.p0 = p0;
        this.polynomialForms = new ArrayList<>();
        this.yval = yval;

        // We describe the y-coordinate with one polynomial
        // [P_1] - yval = 0
        this.polynomialForms.add(new PolynomialForm("{1}" + p0.yvar() + "1+{" + -1 * yval + "}"));
    }

    @Override
    public String showHuman() {
        return String.format("<%s %s : %s(y) = %s>", type, name, p0.showShort(), yval);
    }
}
