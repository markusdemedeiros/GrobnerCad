package ui.gui.mainwindow.exceptions;

// Exceptions for GUI actions that could not be preformed
public class BadCreationActionException extends Exception {
    public BadCreationActionException(String msg) {
        super(msg);
    }
}
