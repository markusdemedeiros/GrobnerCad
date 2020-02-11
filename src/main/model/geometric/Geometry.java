package model.geometric;

import java.util.ArrayList;

// Abstract class to specify a geometric object in space
public abstract class Geometry {

    protected String name;              // Name of instance of reference
    protected String type;              // Type of geometery
    protected int numVariables;         // Number of variables associated with geometery

    // ==========================================================
    // CONSTANTS FOR PREDEFINED OBJECTS
    //      Must be specified individually for generic objects
    //      Variables are strings of the form NAME_index

    // All points have two variables
    //      1: x co-ordinate
    //      2: y-co-ordinate
    public static final String TYPE_POINT = "PT";
    public static final int VARS_POINT = 2;

    // ==========================================================
    // UI RELATED COMMANDS

    // EFFECTS: Returns string describing type and name of the object
    public String show() {
        return "[" + type + " : " + name + "]";
    }

    // EFFECTS: Returns string with any additional useful heuristic information for humans, if applicible.
    public abstract String showHuman();

    // EFFECTS: Returns short string formatted to be included in strings describing constraints
    public String showShort() {
        return "[" + type + " : " + name + "]";
    }

    // EFFECTS: Gets the name of a variable (real number) in it's standardized form
    // REQUIRES: index < numVariables
    //      Otherwise, the string will be a reference to a variable which has no meaning
    protected String getVariable(int index) {
        return String.format("[%s_%d]", name, index);
    }

    // ==========================================================
    // GETTERS/SETTERS
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getNumVariables() {
        return numVariables;
    }
}
