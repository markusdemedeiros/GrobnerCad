package ui;

import model.geometric.Geometry;

import java.util.List;

// Methods pertaining to creating a uniform CLI experience
//      * All elements are designed to work with print(), not println() *
public abstract class UIElm {

    // EFFECTS: Prints a menu to the screen
    // REQUIRES: entries be an even length list of alternating option/value pairs
    public static void options(List<String> entries) {
        String output = "";
        for (int i = 0; i < entries.size() / 2; i++) {
            output += String.format("\t%s -> %s\n", entries.get(2 * i), entries.get(2 * i + 1));
        }
        System.out.print(output);
    }

    // EFFECTS: Prints a title bar to the screen
    public static void title(String titleText) {
        System.out.print(titleText + "\n========================================\n");
    }

    // EFFECTS: Prints a list of geometric elements
    public static void showElements(List<Geometry> elements) {
        String output = "";
        for (Geometry elm : elements) {
            output += String.format("\t%s\n", elm.show());
        }
        System.out.print(output);
    }




}
