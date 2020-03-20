package ui.gui.mainwindow.exceptions;

public class BadPositionException extends BadCreationActionException {
    public BadPositionException() {
        super("Position must be real number");
    }
}
