package edu.unac.exception;

public class NotEnoughResultsException extends RuntimeException {

    public NotEnoughResultsException(String message) {
        super(message);
    }
}