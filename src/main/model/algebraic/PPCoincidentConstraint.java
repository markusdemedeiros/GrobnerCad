package model.algebraic;

import model.calculational.PolynomialForm;
import model.geometric.Point;

import java.util.ArrayList;

// Constraint to descrobe two points being in the same location
//      Same effect as distance = 0, but more efficient
public class PPCoincidentConstraint extends Constraint {
    Point p1;   // The first point
    Point p2;   // The second point

    // MODIFIES: this
    public PPCoincidentConstraint(String name, Point p1, Point p2) {
        // Set local variables
        this.type = PP_COINCIDENT_TYPE;
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
        this.polynomialForms = new ArrayList<>();

        // In general, we describe point coincidence by two equations
        // [A_1]-[B_1] = 0
        this.polynomialForms.add(new PolynomialForm(String.format("{1}%s1+{-1}%s1", p1.xvar(), p2.xvar())));
        // [A_2]-[B_2] = 0
        this.polynomialForms.add(new PolynomialForm(String.format("{1}%s1+{-1}%s1", p1.yvar(), p2.yvar())));
    }


    @Override
    public String showHuman() {
        return String.format("<%s %s : %s = %s>", type, name, p1.showShort(), p2.showShort());
    }


    public Point getP2() {
        return p2;
    }

    public Point getP1() {
        return p1;
    }
}
