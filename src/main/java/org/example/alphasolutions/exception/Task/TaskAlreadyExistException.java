package org.example.alphasolutions.exception.Task;

public class TaskAlreadyExistException extends TaskException {

    private final String identifier;

    public TaskAlreadyExistException(String identifier) {
        super("Opgaven med navn '" + identifier + "' eksisterer allerede");
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
