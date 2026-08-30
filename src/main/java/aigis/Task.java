package aigis;

/**
 * Represents an objective managed by Aigis.
 */
public class Task {
    /** The text describing this task. */
    private final String description;
    /** Whether this task has been completed. */
    private boolean isDone;

    /**
     * Creates an unfinished task with the given description.
     *
     * @param description The text describing the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the marker used to show whether this task is complete.
     *
     * @return {@code X} for a completed task, or a blank space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task description.
     *
     * @return This task's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets whether this task is complete.
     *
     * @param isDone The new completion state.
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns the display form used by Aigis when showing a task.
     *
     * @return The status marker followed by the task description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
