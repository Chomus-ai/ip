package aigis.task;

/**
 * Represents a task without a deadline or event time.
 */
public class Todo extends Task {
    /**
     * Creates an unfinished todo task.
     *
     * @param description The text describing the task.
     * @throws IllegalArgumentException If the description is null or blank.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    protected String getStorageType() {
        return "T";
    }

    @Override
    public String toString() {
        return "[T] " + super.toString();
    }
}
