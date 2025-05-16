package org.example.alphasolutions.exception;

public class InsufficientHoursException extends RuntimeException {
    public InsufficientHoursException() {
        super("Der er ikke tilstrækkelige timer tilbage");
    }
}
