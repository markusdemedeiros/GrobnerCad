package model.persistence;

import model.algebraic.*;
import model.calculational.FullSystem;
import model.geometric.Geometry;
import model.geometric.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Class to test Writer
public class WriterTest {

    private List<Geometry> testGeometery;
    private List<Constraint> allconstraints;
    private FullSystem fs;


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

        fs = new FullSystem(testGeometery, allconstraints);
    }

    // Test write
    // REQUIRES: Read works. This is alright, because the READ tests
    //          are independent of WRITE. A failed READ will however impact both tests,
    @Test
    public void testWriteAssumingRead() {
        try {
            // Clear out file first
            File f = new File ( "./data/test/TESTFILE.sys");
            if (f.exists()){
                f.delete();
            }

            // Write
            Writer wr = new Writer(f);
            wr.write(fs);
            wr.close();

            // Read, then test equivalence
            FullSystem fsread = Reader.readSystem(new File("./data/test/TESTFILE.sys"));
            assertEquals(fs.getGeometery().size(), fsread.getGeometery().size());
            assertEquals(fs.getAlgebra().size(), fsread.getAlgebra().size());
            for (Geometry g :fs.getGeometery()) {
                boolean testpasssed = false;
                for (Geometry x : fsread.getGeometery()) {
                    testpasssed = testpasssed || (g.getType().equals(x.getType())
                            && g.getName().equals(x.getName()));
                }
                assertTrue(testpasssed);
            }
            for (Constraint c : fs.getAlgebra()) {
                boolean testpassed = false;
                for (Constraint x : fsread.getAlgebra()) {
                    testpassed = testpassed || (x.show().equals(c.show()));
                }
                assertTrue(testpassed);
            }
        } catch (FileNotFoundException e) {
            fail("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            fail("File IO Error");
            e.printStackTrace();
        }
    }

}
