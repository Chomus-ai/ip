package aigis;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    /** The time by which this task should be completed. */
    private final String due;

    /**
     * Creates an unfinished deadline task.
     *
     * @param description The text describing the task.
     * @param due The time by which the task should be completed.
     */
    public Deadline(String description, String due) {
        super(description);
        this.due = due;
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " ( by: " + due + " )";
    }
}
