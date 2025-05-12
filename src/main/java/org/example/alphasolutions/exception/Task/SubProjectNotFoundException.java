package org.example.alphasolutions.exception.Task;

public class SubProjectNotFoundException extends TaskException {

    private final String subProjectId;

    public SubProjectNotFoundException(String subProjectId) {
        super("Delprojektet med ID '" + subProjectId + "' kunne ikke findes");
        this.subProjectId = subProjectId;
    }

    public String getSubProjectId() {
        return subProjectId;
    }
}
