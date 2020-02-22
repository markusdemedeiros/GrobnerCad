package model.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

// A writer that can save data to a file
// Based on the TellerApp example
public class Writer {

    private PrintWriter printWriter;

    // Effects: Initialises writer, throws FileNotFound
    // REQUIRES System supports UTF-8.
    public Writer(File file) throws FileNotFoundException {
        try {
            printWriter = new PrintWriter(file, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
