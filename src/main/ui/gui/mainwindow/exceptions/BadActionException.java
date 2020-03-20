package ui.gui.mainwindow.exceptions;

// Exceptions for GUI actions that could not be preformed
public class BadActionException extends Exception {
    public BadActionException(String msg) {
        super(msg);
    }
}
