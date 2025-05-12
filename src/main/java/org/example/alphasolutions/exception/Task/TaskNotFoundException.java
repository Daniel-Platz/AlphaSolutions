package org.example.alphasolutions.exception.Task;

public class TaskNotFoundException extends TaskException {

    private final String subProjectId;
    private final String taskName;

    public TaskNotFoundException(String subProjectId, String taskName) {
        super("Opgave med navn '" + taskName + "' kunne ikke findes i delprojektet med ID '" + subProjectId + "'.");
        this.subProjectId = subProjectId;
        this.taskName = taskName;
    }

    public String getSubProjectId() {
        return subProjectId;
    }

    public String getTaskName(){
        return taskName;
    }
}
