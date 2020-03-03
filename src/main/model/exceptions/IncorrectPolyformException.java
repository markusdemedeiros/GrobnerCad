package model.exceptions;

// Exception for misformatted polynomial form
public class IncorrectPolyformException extends Exception {
    public IncorrectPolyformException() {}
    public IncorrectPolyformException(String message) {
        super(message);
    }
}
