package model.geometric;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeometryTest {

    @Test
    public void testPoint() {
        Point pt = new Point("A");
        geometryImplementationTest(pt, "[PT : A]", "[PT : A]", "[PT : A]");

        assertEquals("PT", pt.getType());
    }

    // Tests if geometry was implemented correctly
    // Everything in the rest of the abstract class is just simple getters/setters
    //      will be covered by other tests
    private void geometryImplementationTest(Geometry testGeometery, String show, String showHuman, String showShort) {
        assertEquals(show, testGeometery.show());
        assertEquals(showHuman, testGeometery.showHuman());
        assertEquals(showShort, testGeometery.showShort());
    }

}
