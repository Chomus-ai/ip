package aigis;

/**
 * Represents a task that occurs between a start and end time.
 */
public class Event extends Task {
    /** The start time of this event. */
    private final String from;
    /** The end time of this event. */
    private final String till;

    /**
     * Creates an unfinished event task.
     *
     * @param description The text describing the task.
     * @param from The start time of the event.
     * @param till The end time of the event.
     */
    public Event(String description, String from, String till) {
        super(description);
        this.from = from;
        this.till = till;
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + "( from: " + from + " to: " + till + " )";
    }
}
