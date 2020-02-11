package model.geometric;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {
    Point p0;

    @BeforeEach
    public void setupTest() {
        p0 = new Point("T");
    }

    // Point is primarily a constructor, all tests can be in one.
    @Test
    public void pointTest() {
        assertEquals("T", p0.getName());
        assertEquals("PT", p0.getType());
        assertEquals(2, p0.getNumVariables());
        assertEquals("[PT : T]", p0.showHuman());
        assertEquals("[T_1]", p0.xvar());
        assertEquals("[T_1]", p0.getVariable(1));
        assertEquals("[T_2]", p0.yvar());
    }

}
