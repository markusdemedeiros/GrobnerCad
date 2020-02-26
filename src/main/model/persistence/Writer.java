package model.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

// A writer that can save data to a file
// ***Based on the TellerApp example*** I could not think of any more appropriate
//          way to implement this, it is pretty simple
public class Writer {

    private PrintWriter printWriter;

    // Effects: Initialises writer, throws FileNotFound
    public Writer(File file) throws FileNotFoundException, UnsupportedEncodingException {
        printWriter = new PrintWriter(file, "UTF-8");
    }

    // EFFECTS: Saves obj to disk as specified by obj
    // MODIFIES: this
    public void write(Saveable obj) {
        obj.save(printWriter);
    }

    // EFFECTS: Closes printwriter stream
    // MODIFIES: this
    public void close() {
        printWriter.close();
    }


}
