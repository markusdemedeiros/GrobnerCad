package model.algebraic;

import model.calculational.PolynomialForm;
import model.geometric.Point;

import java.util.ArrayList;

// Constraint to describe two points being a specified distance apart
public class PPDistanceConstraint extends Constraint {
    Point p1;           // The first point
    Point p2;           // The second point
    double distance;    // The distance between the points

    // MODIFIES: this
    // REQUIRES: Distance be positive (negative distance is stupid)
    public PPDistanceConstraint(String name, Point p1, Point p2, double distance) {
        // Set local variables
        this.type = PP_DISTANCE_TYPE;
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
        this.distance = distance;
        this.polynomialForms = new ArrayList<PolynomialForm>();

        // Given p1 = A, p2 = B, we represent this with a single polynomial
        // ([A_1]-[B_1])^2+([A_2]-[B_2])^2-D = 0
        //      [A_1]^2 - 2[A_1][B_1] + [B_1]^2 + [A_2]^2 - 2[A_2][B_2] + [B_2]^2 - D
        // =>   [A_1]2+{-2}[A_1]1[B_1]+[B_1]2+[A_2]2+{-2}[A_2]1[B_2]1+[B_2]2+{-D}
        this.polynomialForms.add(new PolynomialForm(String.format(
                "{1}%s2+{-2}%s1%s1+{1}%s2+{1}%s2+{-2}%s1%s1+{1}%s2+{%s}",
                p1.xvar(),
                p1.xvar(),
                p2.xvar(),
                p2.xvar(),
                p1.yvar(),
                p1.yvar(),
                p2.yvar(),
                p2.yvar(),
                -1 * distance * distance)));
    }

    @Override
    public String showHuman() {
        return String.format("<%s %s : %s - %s - %s>", type, name, p1.showShort(), distance, p2.showShort());
    }
}
