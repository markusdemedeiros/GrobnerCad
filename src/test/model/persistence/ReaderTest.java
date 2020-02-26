package model.persistence;

import model.FullSystem;
import model.algebraic.*;
import model.geometric.Geometry;
import model.geometric.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {
    private List<Geometry> testGeometery;
    private List<Constraint> allconstraints;

    @BeforeEach
    public void setupTests() {
        testGeometery = new ArrayList<>();
        testGeometery.add(new Point("A"));
        testGeometery.add(new Point("B"));

        allconstraints = Arrays.asList(new PSetXConstraint("XV",
                        (Point) testGeometery.get(0),
                        10),
                new PSetYConstraint("YV",
                        (Point) testGeometery.get(0),
                        10),
                new PPHorizontalConstraint("HORIZ",
                        (Point) testGeometery.get(0),
                        (Point) testGeometery.get(1)),
                new PPVerticalConstraint("VERT",
                        (Point) testGeometery.get(0),
                        (Point) testGeometery.get(1)),
                new PPDistanceConstraint("DIST",
                        (Point) testGeometery.get(0),
                        (Point) testGeometery.get(1),
                        10),
                new PPCoincidentConstraint("COINC",
                        (Point) testGeometery.get(0),
                        (Point) testGeometery.get(1)));
    }

    @Test
    public void testReaderNoException() {
        FullSystem fs = null;
        try {
            fs = Reader.readSystem(new File("./data/allconstraints.sys"));
        } catch (IOException e) {
            fail("File IO Error");
        }

        // Check correct interpretation of geometric elements by comparing
        //      injectivity and size of finite sets whose elements I know to be unique
        assertEquals(testGeometery.size(), fs.getGeometery().size());

        // I haven't overrided equals, so I am happy to just compare type and name
        //      Anything that passes this is more than functionally identical anyways
        for (Geometry g : fs.getGeometery()) {
            boolean testpasssed = false;
            for (Geometry c : testGeometery) {
                testpasssed = testpasssed || (c.getType().equals(g.getType())
                        && c.getName().equals(g.getName()));
            }
            assertTrue(testpasssed); // true => at least one identical element
        }

        // Do the same for the constraints
        assertEquals(allconstraints.size(), fs.getAlgebra().size());
        for (Constraint c : fs.getAlgebra()) {
            boolean testPassed = false;
            for (Constraint x : allconstraints) {
                testPassed = testPassed || (x.show().equals(c.show()));
            }
            assertTrue(testPassed);
        }
    }
}
