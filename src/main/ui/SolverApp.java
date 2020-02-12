package ui;

import model.*;
import model.algebraic.*;
import model.geometric.Geometry;
import model.geometric.Point;

import javax.print.PrintService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// Geometric Constraint Solver Application
// Contains logic for application run, specific UI elements relegated to UIElm class
public class SolverApp {
    private Scanner input;
    private Solver solver;
    private ArrayList<Geometry> geoElements;
    private ArrayList<Constraint> geoConstraints;
    private static final String PROMPT = "  >> ";

    // EFFECTS: Runs the solver application
    public SolverApp() {
        input = new Scanner(System.in);
        geoElements = new ArrayList<>();        // TODO: add default co-ordinate system
        geoConstraints = new ArrayList<>();

        UIElm.title("Welcome to the Grobner Solver");
        System.out.print("View help.md to read about this program!");
        while (getData()) {
            Solver s = new Solver(geoElements, geoConstraints);
            System.out.println("Solving the system of constraints: ");
            for (Constraint k : geoConstraints) {
                System.out.println(k.show());
            }
            Analyst analyst = s.solve();
            System.out.println("Solution is consistent: " + !analyst.isInconsistent());
            System.out.println("Your ideal of solutions:");
            System.out.println(analyst.showSolution());
            System.out.println();
            reset();
        }
        System.out.println("Thank you for using my constriant solver!");
    }

    // MODIFIES: this
    // EFFECTS: returns true if all data was entered properly and we are ready to solve the constrain problem
    private boolean getData() {
        // short-circuiting allows program to halt if any step fails (or quits)
        return (getGeometry()
                && checkGeometery()
                && getConstraints());
    }

    // MODIFIES: this
    // EFFECTS: Processes user's geometric input
    //              Returns true if the program is to continue with the solution, false if it is to exit
    private boolean getGeometry() {
        boolean keepGoing = true;
        String command = null;
        System.out.println("Specifying new constraint problem:");
        System.out.println("(1/5) Select objects:");
        UIElm.options(new ArrayList<String>(Arrays.asList(
                "p", "New Point",
                "n", "All geometry specified",
                "q", "Quit program")));

        while (keepGoing) {
            command = getOption("Specify next object");
            if (command.equals("p")) {
                addPoint();
            } else if (command.equals("n")) {
                keepGoing = false;
            } else if (command.equals("q")) {
                return false;
            } else {
                System.out.println("I'm sorry, I don't understand " + command);
            }
        }
        return true;
    }

    // EFFECTS: Returns true if geometric objects are valid, and reports to user.
    //              Currently just checks that it is nonempty
    private boolean checkGeometery() {
        if (geoElements.size() == 0) {
            System.out.println("Empty geometery gives me nothing to to :(. Quitting program. ");
            return false;
        }
        System.out.println("All geometery specified!");
        System.out.println();
        return true;
    }

    // EFFECTS: Prints constraint options to screen
    private void showConstraintOptions() {
        System.out.println("(2/5) Select Constraints");
        UIElm.options(new ArrayList<>(Arrays.asList(
                "d", "Distance Constraint (two points)",
                "c", "Incidence Constraint (two points)",
                "h", "Horizontal Constraint (two points)",
                "v", "Vertical Constraint (two points)",
                "x", "Point X value (one point)",
                "y", "Point Y value (one point)",
                "n", "All constraints specified",
                "q", "Quit program")));

        System.out.println("Your specified elements: ");
        UIElm.showElements(geoElements);
    }

    // REQUIRES: elms is not empty
    // MODIFIES: this
    // EFFECTS: Prompts user to specify geometric constraints
    private boolean getConstraints() {
        boolean keepGoing = true;
        showConstraintOptions();
        while (keepGoing) {
            String command = getOption("Specify next constraint");
            boolean commandExcecuted = addConstraintIfPossible(command);
            if (!commandExcecuted)  {
                if (command.equals("n")) {
                    keepGoing = false;
                } else if (command.equals("q")) {
                    return false;
                } else {
                    System.out.println("You typed an invalid command, I read: " + command);
                }
            }
        }
        return true;
    }

    // There is no rational reason to have this seperate from getConstraints, but checkstyle hates me personally.
    // EFFECTS: adds constraint if possible, otherwise returns false
    // MODIFIES: this
    private boolean addConstraintIfPossible(String command) {
        if (command.equals("d")) {
            addPPDistance();
        } else if (command.equals("c")) {
            addPPCoincident();
        } else if (command.equals("h")) {
            addPPHorizontal();
        } else if (command.equals("v")) {
            addPPVertical();
        } else if (command.equals("x")) {
            addPSetXConstraint();
        } else if (command.equals("y")) {
            addPSetYConstraint();
        } else {
            return false;
        }
        return true;
    }

    // TODO: Combine some of these into more general options (p/coord, pp/geo)
    // EFFETCS: Prompts user to add PPCoincident constraint
    // MODIFIES: this
    private void addPPCoincident() {
        System.out.println("Enter constraint name: ");
        System.out.print(PROMPT);
        String name = input.next();
        System.out.println();
        Point p1 = getPoint();
        Point p2 = getPoint();
        PPCoincidentConstraint p = new PPCoincidentConstraint(name, p1, p2);
        geoConstraints.add(p);
        System.out.println(String.format("Added constraint %s\n", p.showHuman()));
    }

    // EFFETCS: Prompts user to add PPDistance constraint
    // MODIFIES: this
    private void addPPDistance() {
        System.out.println("Enter constraint name: ");
        System.out.print(PROMPT);
        String name = input.next();
        System.out.println();
        Point p1 = getPoint();
        Point p2 = getPoint();
        System.out.println("Enter distance value");
        System.out.print(PROMPT);
        double distance = input.nextDouble();
        System.out.println();
        PPDistanceConstraint p = new PPDistanceConstraint(name, p1, p2, distance);
        geoConstraints.add(p);
        System.out.println(String.format("Added constraint %s\n", p.showHuman()));
    }

    // EFFETCS: Prompts user to add Psetx constraint
    // MODIFIES: this
    private void addPSetXConstraint() {
        System.out.println("Enter constraint name: ");
        System.out.print(PROMPT);
        String name = input.next();
        System.out.println();
        Point p1 = getPoint();
        System.out.println("Enter x value");
        System.out.print(PROMPT);
        double x = input.nextDouble();
        System.out.println();
        PSetXConstraint p = new PSetXConstraint(name, p1, x);
        geoConstraints.add(p);
        System.out.println(String.format("Added constraint %s\n", p.showHuman()));
    }

    // EFFETCS: Prompts user to add Psety constraint
    // MODIFIES: this
    private void addPSetYConstraint() {
        System.out.println("Enter constraint name: ");
        System.out.print(PROMPT);
        String name = input.next();
        System.out.println();
        Point p1 = getPoint();
        System.out.println("Enter y value");
        System.out.print(PROMPT);
        double y = input.nextDouble();
        System.out.println();
        PSetYConstraint p = new PSetYConstraint(name, p1, y);
        geoConstraints.add(p);
        System.out.println(String.format("Added constraint %s\n", p.showHuman()));
    }

    // EFFETCS: Prompts user to add PPHorizontal constraint
    // MODIFIES: this
    private void addPPHorizontal() {
        System.out.println("Enter constraint name: ");
        System.out.print(PROMPT);
        String name = input.next();
        System.out.println();
        Point p1 = getPoint();
        Point p2 = getPoint();
        PPHorizontalConstraint p = new PPHorizontalConstraint(name, p1, p2);
        geoConstraints.add(p);
        System.out.println(String.format("Added constraint %s\n", p.showHuman()));
    }

    // EFFETCS: Prompts user to add PPVertical constraint
    // MODIFIES: this
    private void addPPVertical() {
        System.out.println("Enter constraint name: ");
        System.out.print(PROMPT);
        String name = input.next();
        System.out.println();
        Point p1 = getPoint();
        Point p2 = getPoint();
        PPVerticalConstraint p = new PPVerticalConstraint(name, p1, p2);
        geoConstraints.add(p);
        System.out.println(String.format("Added constraint %s\n", p.showHuman()));
    }

    // EFFECTS: Gets an option from the user when case doesn't matter
    private String getOption(String prompt) {
        String command;
        System.out.println(prompt);
        System.out.print(PROMPT);
        command = input.next().toLowerCase();
        System.out.println();
        return command;
    }

    // EFFECTS: Prompts the user to select a point from the list of specified points
    // REQUIRES: the list have at least one point
    private Point getPoint() {
        String name = "";
        Point selected = null;

        System.out.println("Enter the name of a point");
        System.out.print(PROMPT);
        name = input.next();
        System.out.println();

        for (Geometry element : geoElements) {
            if (element.getType() == Geometry.TYPE_POINT && element.getName().equals(name)) {
                selected = (Point) element;
                return selected;
            }
        }
        System.out.println(String.format("Point %s not found", name));
        return getPoint();
    }

    // MODIFIES: this
    // EFFECTS: Adds a new point object to list of constraints
    private void addPoint() {
        // ADD DUPLICATE NAME SENSING?
        System.out.println("Enter point name: ");
        System.out.print(PROMPT);
        String name = input.next();
        Point p = new Point(name);
        geoElements.add(p);
        System.out.println("Point " + p.showHuman() + " added!");
        System.out.println();
    }


    // EFFECTS: Resets the geometry
    // MODIFIES: this
    private void reset() {
        this.geoElements = new ArrayList<>();
        this.geoConstraints = new ArrayList<>();
    }

}
