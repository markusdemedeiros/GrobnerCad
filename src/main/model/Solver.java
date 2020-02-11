package model;

import edu.jas.application.Ideal;
import edu.jas.arith.BigDecimal;
import edu.jas.poly.*;
import model.geometric.Geometry;
import model.algebraic.Constraint;

import java.util.ArrayList;
import java.util.List;

// Class which handles the solving of a constraint problem
public class Solver {
    // Independent fields
    private ArrayList<Geometry> geoElements;                // List of geometric elements
    private ArrayList<Constraint> geoConstraints;           // List of geometric constraints
    private GenPolynomialRing polyRing;                     // Poynomial Ring to generate polynomials

    // Dependent fields
    private ArrayList<String> variableDict;                 // Ordered list of all variable names (global)
    private ArrayList<GenPolynomial<BigDecimal>> polyList;  // List of all polynomials to solve (global)

    // Fields pertaining to solution
    private Ideal<BigDecimal> solvedIdeal;


    // MODIFIES: this
    // REQUIRES: All constraints in list are in terms of elements in geoElements
    public Solver(List<Geometry> geoElements, List<Constraint> geoConstraints) {
        // Initialize and copy over arrays
        this.geoElements = new ArrayList<>();
        for (Geometry element : geoElements) {
            this.geoElements.add(element);
        }
        this.geoConstraints = new ArrayList<>();
        for (Constraint constaint : geoConstraints) {
            this.geoConstraints.add(constaint);
        }
        polyRing = new GenPolynomialRing<BigDecimal>(new BigDecimal(), 0);
        polyList = new ArrayList<GenPolynomial<BigDecimal>>();
        variableDict = new ArrayList<String>();

        // Generate dictionary of variable names
        generateDict();
        // Iterate over all constraints
        for (Constraint con : geoConstraints) {
            List<PolynomialForm> conforms = con.getPolynomialForms();
            // Add each equation from each constraint into the system in system form
            for (PolynomialForm pf : conforms) {
                polyList.add(pf.getSystemForm(polyRing, variableDict));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Solves the polynomial list to a Grobner Ideal, returns an Analyst for analysis
    public Analyst solve() {
        PolynomialList<BigDecimal> plst = new PolynomialList<BigDecimal>(polyRing, polyList);
        Ideal<BigDecimal> ideal = new Ideal<BigDecimal>(plst);
        System.out.println("[SOLVER LOG]: PRE-SOLVE POLYNOMIALS");
        for (GenPolynomial p : ideal.getList()) {
            System.out.println("\t" + p.toString());
        }
        solvedIdeal = ideal.GB();  //Ouch, there goes my CPU
        return new Analyst(solvedIdeal, variableDict);
    }

    // EFFECTS: Creates dictionary of variable names
    // MODIFIES: this
    // TODO: Kill all unused variables
    private void generateDict() {
        for (Geometry g : geoElements) {
            String name = g.getName();
            for (int i = 1; i <= g.getNumVariables(); i++) {
                variableDict.add(name + "_" + i);
            }
        }
    }

//    // EFFECTS: Returns index of name in list, -1 otherwise
//    // REQUIRES: name is in dictionary
//    private int dictLookup(String name) {
//        for (int i = 0; i < variableDict.size(); i++) {
//            if (variableDict.get(i).equals(name)) {
//                return i;
//            }
//        }
//        return -1;
//    }

}
