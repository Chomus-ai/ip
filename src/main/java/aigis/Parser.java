package aigis;

import aigis.task.Deadline;
import aigis.task.Event;
import aigis.task.Task;
import aigis.task.Todo;

/**
 * Interprets user input and creates commands for Aigis to execute.
 */
public class Parser {
    private static final String LIST_COMMAND = "list";
    private static final String BYE_COMMAND = "bye";
    private static final String MARK_PREFIX = "mark ";
    private static final String UNMARK_PREFIX = "unmark ";
    private static final String DELETE_PREFIX = "delete ";
    private static final String TODO_PREFIX = "todo ";
    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String EVENT_PREFIX = "event ";
    private static final String BY_MARKER = "/by";
    private static final String FROM_MARKER = "/from";
    private static final String TO_MARKER = "/to";
    private static final String DEADLINE_USAGE = "Please use: deadline <description> /by <date>";
    private static final String EVENT_USAGE = "Please use: event <description> /from <start> /to <end>";
    private static final String UNKNOWN_COMMAND_MESSAGE = "I don't understand that command.";

    /**
     * Parses a line of user input into an executable command.
     *
     * @param input The input line to parse.
     * @return The command represented by the input.
     */
    public Command parse(String input) {
        if (input.equals(BYE_COMMAND)) {
            return Command.of(CommandType.EXIT);
        }
        if (input.equals(LIST_COMMAND)) {
            return Command.of(CommandType.LIST);
        }
        if (input.startsWith(MARK_PREFIX)) {
            return parseTaskNumber(input, MARK_PREFIX, CommandType.MARK);
        }
        if (input.startsWith(UNMARK_PREFIX)) {
            return parseTaskNumber(input, UNMARK_PREFIX, CommandType.UNMARK);
        }
        if (input.startsWith(DELETE_PREFIX)) {
            return parseTaskNumber(input, DELETE_PREFIX, CommandType.DELETE);
        }
        if (input.startsWith(TODO_PREFIX)) {
            return parseTodo(input);
        }
        if (input.startsWith(DEADLINE_PREFIX)) {
            return parseDeadline(input);
        }
        if (input.startsWith(EVENT_PREFIX)) {
            return parseEvent(input);
        }
        return Command.withMessage(CommandType.UNKNOWN, UNKNOWN_COMMAND_MESSAGE);
    }

    /**
     * Parses the task number in a status or delete command.
     *
     * @param input The complete command input.
     * @param commandPrefix The prefix before the task number.
     * @param commandType The type of command being parsed.
     * @return The parsed command or an invalid command with an explanation.
     */
    private Command parseTaskNumber(String input, String commandPrefix, CommandType commandType) {
        String taskNumber = input.substring(commandPrefix.length()).trim();
        try {
            return Command.withTaskNumber(commandType, Integer.parseInt(taskNumber));
        } catch (NumberFormatException exception) {
            return Command.withMessage(CommandType.INVALID, "Please provide a valid task number.");
        }
    }

    /**
     * Parses a todo command and constructs its task.
     *
     * @param input The complete todo command.
     * @return The parsed add command.
     */
    private Command parseTodo(String input) {
        String description = input.substring(TODO_PREFIX.length()).trim();
        try {
            Task task = new Todo(description);
            return Command.withTask(task, "New objective: " + description);
        } catch (IllegalArgumentException exception) {
            return Command.withMessage(CommandType.ADD, exception.getMessage());
        }
    }

    /**
     * Parses a deadline command and constructs its task.
     *
     * @param input The complete deadline command.
     * @return The parsed add command.
     */
    private Command parseDeadline(String input) {
        String deadlineInput = input.substring(DEADLINE_PREFIX.length());
        int byIndex = deadlineInput.indexOf(BY_MARKER);
        if (byIndex <= 0) {
            return Command.withMessage(CommandType.ADD, DEADLINE_USAGE);
        }
        String description = deadlineInput.substring(0, byIndex).trim();
        String due = deadlineInput.substring(byIndex + BY_MARKER.length()).trim();
        if (description.isEmpty() || due.isEmpty()) {
            return Command.withMessage(CommandType.ADD, DEADLINE_USAGE);
        }
        try {
            Task task = new Deadline(description, due);
            String message = "New objective: " + description + " ( by: " + due + " )";
            return Command.withTask(task, message);
        } catch (IllegalArgumentException exception) {
            return Command.withMessage(CommandType.ADD, exception.getMessage());
        }
    }

    /**
     * Parses an event command and constructs its task.
     *
     * @param input The complete event command.
     * @return The parsed add command.
     */
    private Command parseEvent(String input) {
        String eventInput = input.substring(EVENT_PREFIX.length());
        int fromIndex = eventInput.indexOf(FROM_MARKER);
        int toIndex = eventInput.indexOf(TO_MARKER);
        if (fromIndex <= 0 || toIndex <= fromIndex + FROM_MARKER.length()) {
            return Command.withMessage(CommandType.ADD, EVENT_USAGE);
        }
        String description = eventInput.substring(0, fromIndex).trim();
        String from = eventInput.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
        String till = eventInput.substring(toIndex + TO_MARKER.length()).trim();
        if (description.isEmpty() || from.isEmpty() || till.isEmpty()) {
            return Command.withMessage(CommandType.ADD, EVENT_USAGE);
        }
        try {
            Task task = new Event(description, from, till);
            String message = "New objective: " + description
                    + "( from: " + from + " to: " + till + " )";
            return Command.withTask(task, message);
        } catch (IllegalArgumentException exception) {
            return Command.withMessage(CommandType.ADD, exception.getMessage());
        }
    }

    /**
     * The kinds of commands that the parser can recognize.
     */
    public enum CommandType {
        EXIT,
        LIST,
        MARK,
        UNMARK,
        DELETE,
        ADD,
        INVALID,
        UNKNOWN
    }

    /**
     * Represents one parsed command and the data needed to execute it.
     */
    public static final class Command {
        /** The kind of command represented. */
        private final CommandType type;
        /** The one-based task number, when the command has one. */
        private final int taskNumber;
        /** The task created by an add command, when parsing succeeds. */
        private final Task task;
        /** The response associated with the command, when one is needed. */
        private final String message;

        private Command(CommandType type, int taskNumber, Task task, String message) {
            this.type = type;
            this.taskNumber = taskNumber;
            this.task = task;
            this.message = message;
        }

        private static Command of(CommandType type) {
            return new Command(type, -1, null, null);
        }

        private static Command withTaskNumber(CommandType type, int taskNumber) {
            return new Command(type, taskNumber, null, null);
        }

        private static Command withTask(Task task, String message) {
            return new Command(CommandType.ADD, -1, task, message);
        }

        private static Command withMessage(CommandType type, String message) {
            return new Command(type, -1, null, message);
        }

        /**
         * Returns the command type.
         *
         * @return The command type.
         */
        public CommandType getType() {
            return type;
        }

        /**
         * Returns the one-based task number in this command.
         *
         * @return The task number, or {@code -1} if the command has none.
         */
        public int getTaskNumber() {
            return taskNumber;
        }

        /**
         * Returns the task created by this command.
         *
         * @return The created task, or {@code null} if parsing did not create one.
         */
        public Task getTask() {
            return task;
        }

        /**
         * Returns the response associated with this command.
         *
         * @return The response message, or {@code null} if none is needed.
         */
        public String getMessage() {
            return message;
        }
    }
}
