package model.algebraic;

import model.calculational.PolynomialForm;
import model.geometric.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Class to test that the implementations of my constraints follow all standards I've set
public class ConstraintsTest {

    private Point pOrigin;
    private Point p1;
    private Point p2;


    @BeforeEach
    public void setup() {
        pOrigin = new Point("O");
        p1 = new Point("A");
        p2 = new Point("B");
    }

    @Test
    public void PPCoincidentConstraintTest() {
        PPCoincidentConstraint testConstraint1 = new PPCoincidentConstraint("PPCO1", pOrigin, p1);
        ArrayList<PolynomialForm> correctPolyforms1 = new ArrayList<>();
        correctPolyforms1.add(new PolynomialForm("{1}[O_1]1+{-1}[A_1]1"));
        correctPolyforms1.add(new PolynomialForm("{1}[O_2]1+{-1}[A_2]1"));
        constraintImplementationTest(testConstraint1, correctPolyforms1,
                "<pp-CDT PPCO1 : {1}[O_1]1+{-1}[A_1]1 : {1}[O_2]1+{-1}[A_2]1>",
                "<pp-CDT PPCO1 : [PT : O] = [PT : A]>");

        // Probably not necessary
        PPCoincidentConstraint testConstraint2 = new PPCoincidentConstraint("PPCO2", p1, p2);
        ArrayList<PolynomialForm> correctPolyforms2 = new ArrayList<>();
        correctPolyforms2.add(new PolynomialForm("{1}[A_1]1+{-1}[B_1]1"));
        correctPolyforms2.add(new PolynomialForm("{1}[A_2]1+{-1}[B_2]1"));
        constraintImplementationTest(testConstraint2, correctPolyforms2,
                "<pp-CDT PPCO2 : {1}[A_1]1+{-1}[B_1]1 : {1}[A_2]1+{-1}[B_2]1>",
                "<pp-CDT PPCO2 : [PT : A] = [PT : B]>");
    }

    @Test
    public void PPDistanceConstraintTest() {
        // Distance 0 points test
        PPDistanceConstraint testConstraint1 = new PPDistanceConstraint("PPDS1", pOrigin, p1, 0);
        ArrayList<PolynomialForm> correctPolyforms1 = new ArrayList<>();
        correctPolyforms1.add(new PolynomialForm("{1}[O_1]2+{-2}[O_1]1[A_1]1+{1}[A_1]2+{1}[O_2]2+{-2}[O_2]1[A_2]1+{1}[A_2]2+{-0.0}"));
        constraintImplementationTest(testConstraint1, correctPolyforms1,
                "<pp-DST PPDS1 : {1}[O_1]2+{-2}[O_1]1[A_1]1+{1}[A_1]2+{1}[O_2]2+{-2}[O_2]1[A_2]1+{1}[A_2]2+{-0.0}>",
                "<pp-DST PPDS1 : [PT : O] - 0.0 - [PT : A]>");

        // Nonzero distance points test
        PPDistanceConstraint testConstraint2 = new PPDistanceConstraint("PPDS2", p2, p1, 10);
        ArrayList<PolynomialForm> correctPolyforms2 = new ArrayList<>();
        correctPolyforms2.add(new PolynomialForm("{1}[B_1]2+{-2}[B_1]1[A_1]1+{1}[A_1]2+{1}[B_2]2+{-2}[B_2]1[A_2]1+{1}[A_2]2+{-100.0}"));
        constraintImplementationTest(testConstraint2, correctPolyforms2,
                "<pp-DST PPDS2 : {1}[B_1]2+{-2}[B_1]1[A_1]1+{1}[A_1]2+{1}[B_2]2+{-2}[B_2]1[A_2]1+{1}[A_2]2+{-100.0}>",
                "<pp-DST PPDS2 : [PT : B] - 10.0 - [PT : A]>");
    }

    @Test
    public void PPHorizontalConstraintTest() {
        PPHorizontalConstraint testConstraint1 = new PPHorizontalConstraint("TESTNAME", pOrigin, p1);
        ArrayList<PolynomialForm> correctPolyforms1 = new ArrayList<>();
        correctPolyforms1.add(new PolynomialForm("{1}[O_2]1+{-1}[A_2]1"));
        constraintImplementationTest(testConstraint1, correctPolyforms1,
                "<pp-HOR TESTNAME : {1}[O_2]1+{-1}[A_2]1>",
                "<pp-HOR TESTNAME : [PT : O] - H - [PT : A]>");
    }

    @Test
    public void PPVerticalConstraintTest() {
        PPVerticalConstraint testConstraint1 = new PPVerticalConstraint("TESTNAME", pOrigin, p1);
        ArrayList<PolynomialForm> correctPolyforms1 = new ArrayList<>();
        correctPolyforms1.add(new PolynomialForm("{1}[O_1]1+{-1}[A_1]1"));
        constraintImplementationTest(testConstraint1, correctPolyforms1,
                "<pp-VER TESTNAME : {1}[O_1]1+{-1}[A_1]1>",
                "<pp-VER TESTNAME : [PT : O] - V - [PT : A]>");
    }

    @Test
    public void PSetXConstraintTest() {
        // Zero test
        PSetXConstraint testConstraint1 = new PSetXConstraint("TESTNAME", pOrigin, 0);
        ArrayList<PolynomialForm> correctPolyforms1 = new ArrayList<>();
        correctPolyforms1.add(new PolynomialForm("{1}[O_1]1+{-0.0}"));
        constraintImplementationTest(testConstraint1, correctPolyforms1,
                "<p-STX TESTNAME : {1}[O_1]1+{-0.0}>",
                "<p-STX TESTNAME : [PT : O](x) = 0.0>");

        // Nonzero test
        PSetXConstraint testConstraint2 = new PSetXConstraint("TESTNAME", p1, 5.5);
        ArrayList<PolynomialForm> correctPolyforms2 = new ArrayList<>();
        correctPolyforms2.add(new PolynomialForm("{1}[A_1]1+{-5.5}"));
        constraintImplementationTest(testConstraint2, correctPolyforms2,
                "<p-STX TESTNAME : {1}[A_1]1+{-5.5}>",
                "<p-STX TESTNAME : [PT : A](x) = 5.5>");
    }

    @Test
    public void PSetYConstraintTest() {
        // Zero test
        PSetYConstraint testConstraint1 = new PSetYConstraint("TESTNAME", pOrigin, 0);
        ArrayList<PolynomialForm> correctPolyforms1 = new ArrayList<>();
        correctPolyforms1.add(new PolynomialForm("{1}[O_2]1+{-0.0}"));
        constraintImplementationTest(testConstraint1, correctPolyforms1,
                "<p-STY TESTNAME : {1}[O_2]1+{-0.0}>",
                "<p-STY TESTNAME : [PT : O](y) = 0.0>");

        // Nonzero test
        PSetYConstraint testConstraint2 = new PSetYConstraint("TESTNAME", p1, 5.5);
        ArrayList<PolynomialForm> correctPolyforms2 = new ArrayList<>();
        correctPolyforms2.add(new PolynomialForm("{1}[A_2]1+{-5.5}"));
        constraintImplementationTest(testConstraint2, correctPolyforms2,
                "<p-STY TESTNAME : {1}[A_2]1+{-5.5}>",
                "<p-STY TESTNAME : [PT : A](y) = 5.5>");
    }

    // Generic test to verify constraint implementation
    private void constraintImplementationTest(Constraint testConstraint, List<PolynomialForm> correctPolynomial, String show, String showHuman) {
        assertEquals(show, testConstraint.show());
        assertEquals(showHuman, testConstraint.showHuman());
        // Check that every polynomial form in the object has identical representation to a correct one
        // Super inefficient because I don't know how to override equals
        // Also it's a stupid test on lists of size like three so who cares
        for (PolynomialForm pf : testConstraint.getPolynomialForms()) {
            boolean isCorrect = false;
            for (PolynomialForm pfc : correctPolynomial) {
                isCorrect = isCorrect || pf.is(pfc);
            }
            assertTrue(isCorrect);
        }
        // Check that the lists are the same size
        assertEquals(correctPolynomial.size(), testConstraint.getPolynomialForms().size());
        // We only need set-equality here, order doesn't matter. Two finite sets are equal iff
        //      one is contained in the other and they have finite size so these tests suffice.
        //      See: Pigeonhole principle
    }


}
