package model.algebraic;

import model.calculational.PolynomialForm;
import model.geometric.Point;

import java.util.ArrayList;

// Constraint that specifies two points as being on the same horizontal line
public class PPHorizontalConstraint extends Constraint {
    Point p1;   // The first point
    Point p2;   // The second point

    public PPHorizontalConstraint(String name, Point p1, Point p2) {
        // Set local variables
        this.type = PP_HORIZONTAL_TYPE;
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
        this.polynomialForms = new ArrayList<>();

        // In general, we describe horizontal (1st component) coincidence by 1 equation
        // [A_2]-[B_2] = 0
        this.polynomialForms.add(new PolynomialForm(String.format("{1}%s1+{-1}%s1", p1.yvar(), p2.yvar())));
    }

    @Override
    public String showHuman() {
        return String.format("<%s %s : %s - H - %s>", type, name, p1.showShort(), p2.showShort());
    }


    public Point getP2() {
        return p2;
    }

    public Point getP1() {
        return p1;
    }
}
