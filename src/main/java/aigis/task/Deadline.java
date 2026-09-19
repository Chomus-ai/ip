package aigis.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    /** The format used when displaying a deadline to the user. */
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** The date by which this task should be completed. */
    private final LocalDate due;

    /**
     * Creates an unfinished deadline task.
     *
     * @param description The text describing the task.
     * @param due The date by which the task should be completed.
     * @throws IllegalArgumentException If the description is invalid or the due date is null.
     */
    public Deadline(String description, LocalDate due) {
        super(description);
        if (due == null) {
            throw new IllegalArgumentException("Deadline due date cannot be null.");
        }
        this.due = due;
    }

    /**
     * Returns the date by which this task should be completed.
     *
     * @return The deadline date.
     */
    public LocalDate getDue() {
        return due;
    }

    @Override
    protected String getStorageType() {
        return "D";
    }

    @Override
    protected String getStorageDetails() {
        return getDescription() + "  (" + due + ")";
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " ( by: " + due.format(DISPLAY_FORMAT) + " )";
    }
}
