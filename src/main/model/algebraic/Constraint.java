package model.algebraic;

import model.PolynomialForm;

import java.util.List;

// Describes a general constraint one some geometric objects
public abstract class Constraint {
    // Lists of global constraint types
    public static final String PP_DISTANCE_TYPE = "pp-DST";
    public static final String PP_COINCIDENT_TYPE = "pp-CDT";
    public static final String PP_HORIZONTAL_TYPE = "pp-HOR";
    public static final String PP_VERTICAL_TYPE = "pp-VER";
    public static final String P_SETX_CONSTRAINT = "p-STX";
    public static final String P_SETY_CONSTRAINT = "p-STY";
    //  public static final String P_ORIGIN_TYPE = "p-ORG";

    protected String type;  // Individual constraint type, from above
    protected String name;  // Individual constraint name

    // List of equations describing the constraint
    protected List<PolynomialForm> polynomialForms;

    // EFFECTS: returns a string describing the constraint in terms of it's polynomials
    public String show() {
        String result = "";
        for (PolynomialForm pf : polynomialForms) {
            result += String.format("<%s %s : %s>\n", type, name, pf.toString());
        }
        return result;
    }

    // EFFECTS: returns a string which describes the behavior of the constraint in human terms
    public abstract String showHuman();

    // EFFECTS: Getter for list of polynomialForms
    public List<PolynomialForm> getPolynomialForms() {
        return polynomialForms;
    }

}
