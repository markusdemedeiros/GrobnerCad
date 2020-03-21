package ui.gui.mainwindow.exceptions;

public class NoGraphicsException extends Exception {
    public NoGraphicsException() {
        super("File has no graphical information");
    }
}
