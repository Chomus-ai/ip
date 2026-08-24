/**
 * Represents an objective managed by Aigis.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates an unfinished task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the marker used to show whether this task is complete.
     *
     * @return {@code X} for a completed task, or a blank space otherwise
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Returns the task description.
     *
     * @return this task's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates whether this task is complete.
     *
     * @param state the new completion state
     */
    public void updateStatus(boolean state) {
        isDone = state;
    }

    /**
     * Returns the display form used by Aigis when showing a task.
     *
     * @return the status marker followed by the task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
