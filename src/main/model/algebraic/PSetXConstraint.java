package model.algebraic;

import model.PolynomialForm;
import model.geometric.Point;

import java.util.ArrayList;

// Constraint to fix the x-coordinate of a point
public class PSetXConstraint extends Constraint {
    Point p0;   // The point
    double xval;

    public PSetXConstraint(String name, Point p0, double xval) {
        this.type = P_SETX_CONSTRAINT;
        this.name = name;
        this.p0 = p0;
        this.polynomialForms = new ArrayList<>();
        this.xval = xval;

        // We describe the X-coordinate with one polynomial
        // [P_0] - xval = 0
        this.polynomialForms.add(new PolynomialForm("{1}" + p0.xvar() + "1+{" + -1 * xval + "}"));
    }

    @Override
    public String showHuman() {
        return String.format("<%s %s : %s(x) = %s>", type, name, p0.showShort(), xval);
    }
}
