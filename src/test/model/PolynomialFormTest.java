package model;


import static org.junit.jupiter.api.Assertions.*;

import edu.jas.arith.BigDecimal;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

// Test for Polynomial form
public class PolynomialFormTest {

    private List<String> testVariables6;
    private List<String> testVariables3;

    private PolynomialForm pzero;
    private PolynomialForm pconstant;
    private PolynomialForm plinear;
    private PolynomialForm pnonlinear;
    private PolynomialForm pequalsnonlinear;

    private GenPolynomialRing<BigDecimal> gpr;

    private GenPolynomial<BigDecimal> sfzero;
    private GenPolynomial<BigDecimal> sfconst;
    private GenPolynomial<BigDecimal> sflinear3;
    private GenPolynomial<BigDecimal> sflinear6;
    private GenPolynomial<BigDecimal> sfnonlinear6;

    @BeforeEach
    public void setupTests() {
        testVariables6 = new ArrayList<>();
        testVariables3 = new ArrayList<>();

        // For testing a system in 6 variables
        testVariables6.add("X_1");
        testVariables6.add("X_2");
        testVariables6.add("X_3");
        testVariables6.add("Y_1");
        testVariables6.add("Z_2");
        testVariables6.add("Z_3");

        // For testing a system in 3 variables
        testVariables3.add("X_1");
        testVariables3.add("X_2");
        testVariables3.add("Z_2");

        pzero = new PolynomialForm("{0}");
        pconstant = new PolynomialForm("{5.5}");
        plinear = new PolynomialForm("{1}[X_1]1+{3}[X_2]1+{4}[Z_2]3+{6.0}");
        pnonlinear = new PolynomialForm("{1}[X_1]3[Y_1]2+{-4}[X_2]2[Z_3]1[Z_2]4+{-1.0}");
        pequalsnonlinear = new PolynomialForm("{1}[Y_1]2[X_1]3+{-4}[X_2]2[Z_3]1[Z_2]4+{-1.0}");

        gpr = new GenPolynomialRing<BigDecimal>(new BigDecimal(), 0);
    }

    @Test
    public void testIs() {
        // Two algebraically different polynomials
        assertFalse(pzero.is(pconstant));
        // Two algebraically equal but represented different polynomials
        assertFalse(pnonlinear.is(pequalsnonlinear));
        // Two identical polynomials
        assertTrue(plinear.is(new PolynomialForm("{1}[X_1]1+{3}[X_2]1+{4}[Z_2]3+{6.0}")));
    }

    private void setupPolynomials() {
        sfzero = new GenPolynomial<BigDecimal>(gpr);
        sfconst = new GenPolynomial<BigDecimal>(gpr, new BigDecimal(5.5));
        sflinear3 = new GenPolynomial<BigDecimal>(gpr);
        sflinear3 = sflinear3.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(1), ExpVector.create("(1,0,0)")));
        sflinear3 = sflinear3.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(3), ExpVector.create("(0,1,0)")));
        sflinear3 = sflinear3.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(4), ExpVector.create("(0,0,3)")));
        sflinear3 = sflinear3.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(6), ExpVector.create("(0,0,0)")));
        sflinear6 = new GenPolynomial<BigDecimal>(gpr);
        sflinear6 = sflinear6.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(1), ExpVector.create("(1,0,0,0,0,0)")));
        sflinear6 = sflinear6.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(3), ExpVector.create("(0,1,0,0,0,0)")));
        sflinear6 = sflinear6.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(4), ExpVector.create("(0,0,0,0,3,0)")));
        sflinear6 = sflinear6.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(6), ExpVector.create("(0,0,0,0,0,0)")));
        sfnonlinear6 = new GenPolynomial<BigDecimal>(gpr);
        sfnonlinear6 = sfnonlinear6.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(1), ExpVector.create("(3,0,0,2,0,0)")));
        sfnonlinear6 = sfnonlinear6.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(-4), ExpVector.create("(0,2,0,0,4,1)")));
        sfnonlinear6 = sfnonlinear6.sum(new GenPolynomial<BigDecimal>(gpr, new BigDecimal(-1), ExpVector.create("(0,0,0,0,0,0)")));
    }

    @Test
    public void testGetSystemForm() {
        setupPolynomials();

        // Zero and constant should give the same polynomials irrespective of variable lists
        assertEquals(sfzero, pzero.getSystemForm(gpr, testVariables3));
        assertEquals(sfzero, pzero.getSystemForm(gpr, testVariables6));
        assertEquals(sfconst, pconstant.getSystemForm(gpr, testVariables3));
        assertEquals(sfconst, pconstant.getSystemForm(gpr, testVariables6));

        // Linear polynomial should NOT- variables in 3 and 6 systems in different orders
        //      The different variable lists mean different contexts in which to interpret the strings
        assertEquals(sflinear3, plinear.getSystemForm(gpr, testVariables3));
        assertEquals(sflinear6, plinear.getSystemForm(gpr, testVariables6));
        assertNotEquals(plinear.getSystemForm(gpr, testVariables3),
                plinear.getSystemForm(gpr, testVariables6));

        // Nonlinear test
        //      Also tests that algebraically equivalent polynomials give same representation
        //      when generated in the same context
        assertEquals(sfnonlinear6, pnonlinear.getSystemForm(gpr, testVariables6));
        assertEquals(sfnonlinear6, pequalsnonlinear.getSystemForm(gpr, testVariables6));
    }
}
