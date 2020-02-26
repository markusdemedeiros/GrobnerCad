package model.calculational;

import model.algebraic.*;
import model.calculational.Analyst;
import model.calculational.Solver;
import model.geometric.Geometry;
import model.geometric.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

// Class to test the solver and Analyst, since their funcitonalities are so similar
public class SolverAnalystTest {

    // Geometric Elements
    private ArrayList<Geometry> testGeometery;

    private Solver inconsistentSystem;
    private Solver finiteSystem;
    private Solver infiniteSystem;

    private Analyst inconsistentAnalyst;
    private Analyst finiteAnalyst;
    private Analyst infiniteAnalyst;


    @BeforeEach
    public void setupTests() {
        testGeometery = new ArrayList<>();
        testGeometery.add(new Point("A"));
        testGeometery.add(new Point("B"));
        testGeometery.add(new Point("C"));

        inconsistentSystem = new Solver(testGeometery,
                Arrays.asList(new PPDistanceConstraint("DIST1",
                        (Point) testGeometery.get(0),
                        (Point) testGeometery.get(1),
                        5),
                        new PPDistanceConstraint("DIST2",
                                (Point) testGeometery.get(1),
                                (Point) testGeometery.get(0),
                                6)));

        finiteSystem = new Solver(testGeometery,
                Arrays.asList(
                        new PPDistanceConstraint("DIST1",
                                (Point) testGeometery.get(0),
                                (Point) testGeometery.get(1),
                                3),
                        new PPDistanceConstraint("DIST2",
                                (Point) testGeometery.get(0),
                                (Point) testGeometery.get(2),
                                4),
                        new PSetXConstraint("AX",
                                (Point) testGeometery.get(0),
                                10),
                        new PSetYConstraint("AY",
                                (Point) testGeometery.get(0),
                                10),
                        new PPHorizontalConstraint("HRZ",
                                (Point) testGeometery.get(0),
                                (Point) testGeometery.get(1)),
                        new PPVerticalConstraint("VRT",
                                (Point) testGeometery.get(0),
                                (Point) testGeometery.get(2))));

        infiniteSystem = new Solver (testGeometery,
                Arrays.asList(new PSetXConstraint("AX",
                        (Point) testGeometery.get(0),
                        5),
                        new PSetYConstraint("AY",
                                (Point) testGeometery.get(0),
                                5),
                        new PPHorizontalConstraint("HZ",
                                (Point) testGeometery.get(0),
                                (Point) testGeometery.get(1)),
                        new PPVerticalConstraint("VT",
                                (Point) testGeometery.get(0),
                                (Point) testGeometery.get(1)),
                        new PPDistanceConstraint("DIST",
                                (Point) testGeometery.get(1),
                                (Point) testGeometery.get(2),
                                5)));
    }

    @Test
    public void testInconsistentSystem() {
        inconsistentAnalyst = inconsistentSystem.solve();
        assertTrue(inconsistentAnalyst.isInconsistent());
    }

    @Test
    public void testFiniteSystem() {
        finiteAnalyst = finiteSystem.solve();
        assertFalse(finiteAnalyst.isInconsistent());
    }

    @Test
    public void testInfiniteSystem() {
        infiniteAnalyst = infiniteSystem.solve();
        assertFalse(infiniteAnalyst.isInconsistent());
    }

    // Ensures consistency in the presentation of solutions, move to UI Class?
    @Test
    public void testShowSolution() {
        finiteAnalyst = finiteSystem.solve();
        assertEquals("[ SOLUTION ]\nVARIABLES:\n" +
                "\t0: A_1\n" +
                "\t1: A_2\n" +
                "\t2: B_1\n" +
                "\t3: B_2\n" +
                "\t4: C_1\n" +
                "\t5: C_2\n" +
                "POLYNOMIALS:\n" +
                "\t(0,0,2,0,0,0):long - 20 (0,0,1,0,0,0):long + 91 (0,0,0,0,0,0):long\n" +
                "\t(0,0,0,0,0,2):long - 20 (0,0,0,0,0,1):long + 84 (0,0,0,0,0,0):long\n" +
                "\t(0,0,0,0,1,0):long - 10 (0,0,0,0,0,0):long\n" +
                "\t(0,0,0,1,0,0):long - 10 (0,0,0,0,0,0):long\n" +
                "\t(1,0,0,0,0,0):long - 10 (0,0,0,0,0,0):long\n" +
                "\t(0,1,0,0,0,0):long - 10 (0,0,0,0,0,0):long\n",
                finiteAnalyst.showSolution());

    }


}
