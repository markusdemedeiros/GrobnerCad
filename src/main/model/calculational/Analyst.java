package model.calculational;

import edu.jas.application.Ideal;
import edu.jas.arith.BigDecimal;
import edu.jas.poly.GenPolynomial;

import java.util.List;

// Solver specific class to interpret results after a grobner basis is calculated
// Can only be created by a Solver
public class Analyst {
    private Ideal<BigDecimal> basis;
    private List<String> variableDict;

    public Analyst(Ideal<BigDecimal> basis, List<String> variableDict) {
        this.basis = basis;
        this.variableDict = variableDict;
    }

    // EFFECTS: Returns basis polynomials in human readable format
    public String showSolution() {
        String result = "[ SOLUTION ]\nVARIABLES:\n";
        for (int i = 0; i < variableDict.size(); i++) {
            result += "\t" + i + ": " + variableDict.get(i) + "\n";
        }
        result += "POLYNOMIALS:\n";
        for (GenPolynomial<BigDecimal> polynomial : basis.getList()) {
            result += "\t" + polynomial.toString() + "\n";
        }
        return result;
    }

    // EFFECTS: returns true iff the system is not consistent
    public boolean isInconsistent() {
        for (GenPolynomial<BigDecimal> poly : basis.getList()) {
            if (poly.isConstant()) {
                return true;
            }
        }
        return false;
    }


}
