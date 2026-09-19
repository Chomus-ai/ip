package aigis;

import aigis.task.Deadline;
import aigis.task.Event;
import aigis.task.Task;
import aigis.task.Todo;

/**
 * A simple command-line task manager.
 */
public class Aigis {
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
     * Starts the Aigis command-line task manager.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        try (Ui ui = new Ui()) {
            ui.showWelcome();

            Task[] savedTasks = new Task[TaskList.MAX_TASKS];
            int savedTaskCount = Storage.load(savedTasks);
            TaskList tasks = new TaskList(savedTasks, savedTaskCount);

            try {
                runCommandLoop(tasks, ui);
            } finally {
                Storage.save(tasks.toArray(), tasks.size());
            }
            ui.showGoodbye();
        }
    }

    /**
     * Reads and processes commands until the user exits or input is exhausted.
     *
     * @param tasks The task storage used by the command loop.
     * @param ui The UI used for console input and output.
     */
    private static void runCommandLoop(TaskList tasks, Ui ui) {
        String input;
        while ((input = ui.readCommand()) != null) {
            if (input.equals(BYE_COMMAND)) {
                break;
            }
            processCommand(input, tasks, ui);
        }
    }

    /**
     * Processes one non-exit command.
     *
     * @param input The command entered by the user.
     * @param tasks The task storage used by the command.
     * @param ui The UI used to display command results.
     */
    private static void processCommand(String input, TaskList tasks, Ui ui) {
        if (input.equals(LIST_COMMAND)) {
            ui.showTasks(tasks);
            return;
        }
        if (input.startsWith(MARK_PREFIX)) {
            updateTaskStatus(input, tasks, ui, MARK_PREFIX, true);
            return;
        }
        if (input.startsWith(UNMARK_PREFIX)) {
            updateTaskStatus(input, tasks, ui, UNMARK_PREFIX, false);
            return;
        }
        if (input.startsWith(DELETE_PREFIX)) {
            deleteTask(input, tasks, ui);
            return;
        }
        if (isTaskCreationCommand(input) && tasks.isFull()) {
            ui.showMessage("The task list is full.");
            return;
        }
        if (input.startsWith(TODO_PREFIX)) {
            addTodo(input, tasks, ui);
            return;
        }
        if (input.startsWith(DEADLINE_PREFIX)) {
            addDeadline(input, tasks, ui);
            return;
        }
        if (input.startsWith(EVENT_PREFIX)) {
            addEvent(input, tasks, ui);
            return;
        }
        ui.showMessage(UNKNOWN_COMMAND_MESSAGE);
    }

    /**
     * Updates a task's completion status based on a mark or unmark command.
     *
     * @param input The status command entered by the user.
     * @param tasks The task storage containing the target task.
     * @param ui The UI used to display the result.
     * @param commandPrefix The prefix to remove before parsing the task number.
     * @param isDone The completion status to assign.
     */
    private static void updateTaskStatus(String input, TaskList tasks, Ui ui,
                                         String commandPrefix, boolean isDone) {
        String taskNumber = input.substring(commandPrefix.length()).trim();
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                ui.showMessage("That task does not exist.");
                return;
            }
            tasks.get(taskIndex).setDone(isDone);
            String statusMessage = isDone ? "Marked as done: " : "Unmarked as done: ";
            ui.showMessage(statusMessage + tasks.get(taskIndex));
        } catch (NumberFormatException exception) {
            ui.showMessage("Please provide a valid task number.");
        }
    }

    /**
     * Deletes a task selected by its one-based display number.
     *
     * @param input The delete command entered by the user.
     * @param tasks The task storage from which to remove the target task.
     * @param ui The UI used to display the result.
     */
    private static void deleteTask(String input, TaskList tasks, Ui ui) {
        String taskNumber = input.substring(DELETE_PREFIX.length()).trim();
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                ui.showMessage("That task does not exist.");
                return;
            }
            Task deletedTask = tasks.remove(taskIndex);
            ui.showMessage("Deleted task: " + deletedTask);
        } catch (NumberFormatException exception) {
            ui.showMessage("Please provide a valid task number.");
        }
    }

    /**
     * Adds a todo task if its description is non-empty.
     *
     * @param input The todo command entered by the user.
     * @param tasks The task storage receiving the new task.
     * @param ui The UI used to display the result.
     */
    private static void addTodo(String input, TaskList tasks, Ui ui) {
        String todo = input.substring(TODO_PREFIX.length()).trim();
        try {
            Task newTask = new Todo(todo);
            ui.showMessage("New objective: " + todo);
            tasks.add(newTask);
        } catch (IllegalArgumentException exception) {
            ui.showMessage(exception.getMessage());
        }
    }

    /**
     * Adds a deadline task when both its description and due date are present.
     *
     * @param input The deadline command entered by the user.
     * @param tasks The task storage receiving the new task.
     * @param ui The UI used to display the result.
     */
    private static void addDeadline(String input, TaskList tasks, Ui ui) {
        String deadlineInput = input.substring(DEADLINE_PREFIX.length());
        int byIndex = deadlineInput.indexOf(BY_MARKER);
        if (byIndex <= 0) {
            ui.showMessage(DEADLINE_USAGE);
            return;
        }
        String deadline = deadlineInput.substring(0, byIndex).trim();
        String due = deadlineInput.substring(byIndex + BY_MARKER.length()).trim();
        if (deadline.isEmpty() || due.isEmpty()) {
            ui.showMessage(DEADLINE_USAGE);
            return;
        }
        try {
            Task newTask = new Deadline(deadline, due);
            ui.showMessage("New objective: " + deadline + " ( by: " + due + " )");
            tasks.add(newTask);
        } catch (IllegalArgumentException exception) {
            ui.showMessage(exception.getMessage());
        }
    }

    /**
     * Adds an event task when its description, start time, and end time are present.
     *
     * @param input The event command entered by the user.
     * @param tasks The task storage receiving the new task.
     * @param ui The UI used to display the result.
     */
    private static void addEvent(String input, TaskList tasks, Ui ui) {
        String eventInput = input.substring(EVENT_PREFIX.length());
        int fromIndex = eventInput.indexOf(FROM_MARKER);
        int toIndex = eventInput.indexOf(TO_MARKER);
        if (fromIndex <= 0 || toIndex <= fromIndex + FROM_MARKER.length()) {
            ui.showMessage(EVENT_USAGE);
            return;
        }
        String event = eventInput.substring(0, fromIndex).trim();
        String from = eventInput.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
        String till = eventInput.substring(toIndex + TO_MARKER.length()).trim();
        if (event.isEmpty() || from.isEmpty() || till.isEmpty()) {
            ui.showMessage(EVENT_USAGE);
            return;
        }
        try {
            Task newTask = new Event(event, from, till);
            ui.showMessage("New objective: " + event
                    + "( from: " + from + " to: " + till + " )");
            tasks.add(newTask);
        } catch (IllegalArgumentException exception) {
            ui.showMessage(exception.getMessage());
        }
    }

    /**
     * Checks whether an input begins a command that creates a task.
     *
     * @param input The command to inspect.
     * @return {@code true} if the command creates a task.
     */
    private static boolean isTaskCreationCommand(String input) {
        return input.startsWith(TODO_PREFIX)
                || input.startsWith(DEADLINE_PREFIX)
                || input.startsWith(EVENT_PREFIX);
    }
}
