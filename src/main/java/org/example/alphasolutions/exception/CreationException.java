package org.example.alphasolutions.exception;

public class CreationException extends RuntimeException {
    public CreationException() {
        super("Kunne ikke hente genereret nøgle efter indsættelse");    }
}
