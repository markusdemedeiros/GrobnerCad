package model.algebraic;

import model.calculational.PolynomialForm;
import model.geometric.Point;

import java.util.ArrayList;

// Constraint that specifies two points as being on the same vertical line
public class PPVerticalConstraint extends Constraint {
    Point p1;   // The first point
    Point p2;   // The second point

    public PPVerticalConstraint(String name, Point p1, Point p2) {
        // Set local variables
        this.type = PP_VERTICAL_TYPE;
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
        this.polynomialForms = new ArrayList<>();

        // In general, we describe vertical (2nd component) coincidence by 1 equation
        // [A_1]-[B_1] = 0
        this.polynomialForms.add(new PolynomialForm(String.format("{1}%s1+{-1}%s1", p1.xvar(), p2.xvar())));
    }

    @Override
    public String showHuman() {
        return String.format("<%s %s : %s - V - %s>", type, name, p1.showShort(), p2.showShort());
    }


    public Point getP2() {
        return p2;
    }

    public Point getP1() {
        return p1;
    }
}
