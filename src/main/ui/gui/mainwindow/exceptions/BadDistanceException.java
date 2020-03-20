package ui.gui.mainwindow.exceptions;

public class BadDistanceException extends BadActionException {
    public BadDistanceException() {
        super("Euclidean distances must be non-negative real numbers");
    }
}
