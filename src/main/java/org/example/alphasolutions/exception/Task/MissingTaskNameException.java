package org.example.alphasolutions.exception.Task;

public class MissingTaskNameException extends TaskException {

    public MissingTaskNameException() {
        super("Opgavenavn må ikke være tomt");
    }
}
