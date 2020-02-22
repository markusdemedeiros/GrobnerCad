package model;

import edu.jas.arith.BigDecimal;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;

import java.util.List;

// String which represents a polynomial (in non-global terms)

// POLYNOMIAL FORM STRINGS HAVE A SPECIFIC FORMAT
//      [   ]   encloses variable names in name_index form
//                  and is followed DIRECTLY by integer exponent
//                  EVEN for powers of 1 (but not for 0)
//      {   }   encloses real numbers (preceding [] is coeficient,
//                  preceding nothing is constant
//      +       is the only acceptable algebraic operation, seperating MONOMIALS
//      All polynomials are equated to zero
//      Every term has coeficient
//       EXAMPLE: {3}[A_1]2[B_2]1+{-1}[C_3]5[C_2]2+{-1}
// TODO proper setter which enforces these rules??
public class PolynomialForm {
    private List<String> variables; // Most recent list of variable names
    private String polynomial;      // PolynomialForm formatted representation of the polynomial

    // REQUIRES: polynomial is in the afformentioned format
    // MODIFIES: this
    public PolynomialForm(String polynomial) {
        this.polynomial = polynomial;
    }

    // EFFECTS: Getter for polynomial string
    public String toString() {
        return polynomial;
    }

    // EFFECTS: Returns a GenPolynomial<BigDecimal> from the given polynomial form representation
    // REQUIRES: the stored polynomial is in a valid format, variables contains all variable names in polynomial
    // MODIFIES: this (updates variable names to be current)
    public GenPolynomial<BigDecimal> getSystemForm(GenPolynomialRing<BigDecimal> gpr, List<String> variables) {
        GenPolynomial<BigDecimal> result = new GenPolynomial(gpr);
        this.variables = variables;
        String[] mons = getMonomials();
        for (int i = 0; i < mons.length; i++) {
            String current = mons[i];
            result = result.sum(new GenPolynomial<BigDecimal>(gpr,
                    new BigDecimal(getCoefficient(current)),
                    monomialToExpvector(current)));
        }
        return result;
    }

    // EFFECTS: Returns ordered list of monomials in the stored polynomial
    // REQUIRES: polynomial is in the proper form
    private String[] getMonomials() {
        return polynomial.split("\\+");
    }


    // EFFECTS: Parses and returns the coeficient in a monomial
    // REQUIRES: monomial is in proper format
    private double getCoefficient(String monomial) {
        String resultString = "";
        do {
            monomial = monomial.substring(1);
            resultString += monomial.charAt(0);
        } while (monomial.charAt(1) != '}');
        return Double.parseDouble(resultString);
    }


    // EFFECTS: Returns exponent vector in the stored list of variables for any monomial
    // REQUIRES: monomial is in proper format
    private ExpVector monomialToExpvector(String monomial) {
        int[] exponents = new int[variables.size()];
        String[] terms = monomial.split("\\[");
        // First term is constant, after this they are split name]exp
        for (int i = 1; i < terms.length; i++) {
            String[] atoms = terms[i].split("\\]");
            // First atom is name, second atom is exponent
            int index = dictLookup(atoms[0]);
            int power = Integer.parseInt(atoms[1]);
            exponents[index] = power;
        }
        String cstring = "(";
        for (int i = 0; i < exponents.length; i++) {
            cstring += exponents[i] + ",";
        }
        cstring = cstring.substring(0, cstring.length() - 1) + ")";
        ExpVector result = ExpVector.create(cstring);
        return result;
    }

    // EFFECTS: Returns index of name in variable list, or -1 if not in list
    private int dictLookup(String name) {
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }

    // EFFECTS: Returns true iff one polynomial form is identical in
    //      representation to another. Two "equal" polynomial forms can
    //      have different representations.
    public boolean is(PolynomialForm pf) {
        return (pf.toString().equals(this.toString()));
    }
}
