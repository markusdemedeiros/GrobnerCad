package model.persistence;

import java.io.PrintWriter;

// Class which can be saved to disk, and read as class of type C
//      (So I can say write a solutionset to disk, have it read as type image or
//         have it read as type polynomial etc
public interface Saveable {

    // MODIFIES: Printwriter
    // EFFECTS: Saves class to printwriter
    void save(PrintWriter printWriter);

}
