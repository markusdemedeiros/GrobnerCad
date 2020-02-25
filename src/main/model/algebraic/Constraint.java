package model.algebraic;

import model.PolynomialForm;

import java.util.List;

// Describes a general constraint one some geometric objects
public abstract class Constraint {
    // Lists of global constraint types
    // TODO: Change to dictionary, make it easier on reader
    public static final String PP_DISTANCE_TYPE = "pp-DST";
    public static final String PP_COINCIDENT_TYPE = "pp-CDT";
    public static final String PP_HORIZONTAL_TYPE = "pp-HOR";
    public static final String PP_VERTICAL_TYPE = "pp-VER";
    public static final String P_SETX_CONSTRAINT = "p-STX";
    public static final String P_SETY_CONSTRAINT = "p-STY";
    //  public static final String P_ORIGIN_TYPE = "p-ORG";


    protected String type;  // Individual constraint type, from above
    protected String name;  // Individual constraint name
                                // CANNOT CONTAIN SPECIAL CHARACTERS []{}_()

    // List of equations describing the constraint
    protected List<PolynomialForm> polynomialForms;

    // EFFECTS: returns a string describing the constraint in terms of it's polynomials
    public String show() {
        // Jesus christ, clean this nonsense up. Every constraint has AT MINIMUM one polynomial associatied with it.
        String result = String.format("<%s %s", type, name);
        for (PolynomialForm pf : polynomialForms) {
            result += String.format(" : %s", pf.toString());
        }
        result = result + ">\n";
        return result;
    }

    // EFFECTS: returns a string which describes the behavior of the constraint in human terms
    public abstract String showHuman();

    // EFFECTS: Getter for list of polynomialForms
    public List<PolynomialForm> getPolynomialForms() {
        return polynomialForms;
    }

}
