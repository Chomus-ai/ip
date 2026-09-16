package aigis;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import aigis.task.Deadline;
import aigis.task.Event;
import aigis.task.Task;
import aigis.task.Todo;

/**
 * A simple command-line task manager.
 */
public class Aigis {
    /** Maximum number of tasks that Aigis can store. */
    private static final int MAX_TASKS = 100;
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

    private static final String BANNER =
            """
                          __        __     _______   __      ________ \s
                         /""\\      |" \\   /" _   "| |" \\    /"       )\s
                        /    \\     ||  | (: ( \\___) ||  |  (:   \\___/ \s
                       /' /\\  \\    |:  |  \\/ \\      |:  |   \\___  \\   \s
                      //  __'  \\   |.  |  //  \\ ___ |.  |    __/  \\\\  \s
                     /   /  \\\\  \\  /\\  |\\(:   _(  _|/\\  |\\  /" \\   :) \s
                    (___/    \\___)(__\\_|_)\\_______)(__\\_|_)(_______/  \s
                    
                    Aigis is ready to help!
                    Awaiting commands...
                    """;

    private static final String CLOSING = " ----------------------------------------------------\n"
            + "Tasks completed. See you again soon!\n"
            + "__________________________________________________________\n";

    /**
     * Starts the Aigis command-line task manager.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        System.out.println(BANNER);

        Task[] savedTasks = new Task[MAX_TASKS];
        int savedTaskCount = Storage.load(savedTasks);
        List<Task> tasks = new ArrayList<>(savedTaskCount);
        for (int i = 0; i < savedTaskCount; i++) {
            tasks.add(savedTasks[i]);
        }

        try {
            runCommandLoop(tasks);
        } finally {
            Storage.save(tasks.toArray(new Task[0]), tasks.size());
        }
        System.out.println(CLOSING);
    }

    /**
     * Reads and processes commands until the user exits or input is exhausted.
     *
     * @param tasks The task storage used by the command loop.
     */
    private static void runCommandLoop(List<Task> tasks) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();

                if (input.equals(BYE_COMMAND)) {
                    break;
                }
                processCommand(input, tasks);
            }
        }
    }

    /**
     * Processes one non-exit command.
     *
     * @param input The command entered by the user.
     * @param tasks The task storage used by the command.
     */
    private static void processCommand(String input, List<Task> tasks) {
        if (input.equals(LIST_COMMAND)) {
            printTasks(tasks);
            return;
        }
        if (input.startsWith(MARK_PREFIX)) {
            updateTaskStatus(input, tasks, MARK_PREFIX, true);
            return;
        }
        if (input.startsWith(UNMARK_PREFIX)) {
            updateTaskStatus(input, tasks, UNMARK_PREFIX, false);
            return;
        }
        if (input.startsWith(DELETE_PREFIX)) {
            deleteTask(input, tasks);
            return;
        }
        if (isTaskCreationCommand(input) && tasks.size() >= MAX_TASKS) {
            System.out.println("The task list is full.");
            return;
        }
        if (input.startsWith(TODO_PREFIX)) {
            addTodo(input, tasks);
            return;
        }
        if (input.startsWith(DEADLINE_PREFIX)) {
            addDeadline(input, tasks);
            return;
        }
        if (input.startsWith(EVENT_PREFIX)) {
            addEvent(input, tasks);
            return;
        }
        System.out.println(UNKNOWN_COMMAND_MESSAGE);
    }

    /**
     * Prints every stored task with its one-based display number.
     *
     * @param tasks The task storage to display.
     */
    private static void printTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(i + 1 + ". " + tasks.get(i));
        }
    }

    /**
     * Updates a task's completion status based on a mark or unmark command.
     *
     * @param input The status command entered by the user.
     * @param tasks The task storage containing the target task.
     * @param commandPrefix The prefix to remove before parsing the task number.
     * @param isDone The completion status to assign.
     */
    private static void updateTaskStatus(String input, List<Task> tasks, String commandPrefix,
                                         boolean isDone) {
        String taskNumber = input.substring(commandPrefix.length()).trim();
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                System.out.println("That task does not exist.");
                return;
            }
            tasks.get(taskIndex).setDone(isDone);
            String statusMessage = isDone ? "Marked as done: " : "Unmarked as done: ";
            System.out.println(statusMessage + tasks.get(taskIndex));
        } catch (NumberFormatException exception) {
            System.out.println("Please provide a valid task number.");
        }
    }

    /**
     * Deletes a task selected by its one-based display number.
     *
     * @param input The delete command entered by the user.
     * @param tasks The task storage from which to remove the target task.
     */
    private static void deleteTask(String input, List<Task> tasks) {
        String taskNumber = input.substring(DELETE_PREFIX.length()).trim();
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                System.out.println("That task does not exist.");
                return;
            }
            Task deletedTask = tasks.remove(taskIndex);
            System.out.println("Deleted task: " + deletedTask);
        } catch (NumberFormatException exception) {
            System.out.println("Please provide a valid task number.");
        }
    }

    /**
     * Adds a todo task if its description is non-empty.
     *
     * @param input The todo command entered by the user.
     * @param tasks The task storage receiving the new task.
     */
    private static void addTodo(String input, List<Task> tasks) {
        String todo = input.substring(TODO_PREFIX.length()).trim();
        try {
            Task newTask = new Todo(todo);
            System.out.println("New objective: " + todo);
            tasks.add(newTask);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Adds a deadline task when both its description and due date are present.
     *
     * @param input The deadline command entered by the user.
     * @param tasks The task storage receiving the new task.
     */
    private static void addDeadline(String input, List<Task> tasks) {
        String deadlineInput = input.substring(DEADLINE_PREFIX.length());
        int byIndex = deadlineInput.indexOf(BY_MARKER);
        if (byIndex <= 0) {
            System.out.println(DEADLINE_USAGE);
            return;
        }
        String deadline = deadlineInput.substring(0, byIndex).trim();
        String due = deadlineInput.substring(byIndex + BY_MARKER.length()).trim();
        if (deadline.isEmpty() || due.isEmpty()) {
            System.out.println(DEADLINE_USAGE);
            return;
        }
        try {
            Task newTask = new Deadline(deadline, due);
            System.out.println("New objective: " + deadline + " ( by: " + due + " )");
            tasks.add(newTask);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Adds an event task when its description, start time, and end time are present.
     *
     * @param input The event command entered by the user.
     * @param tasks The task storage receiving the new task.
     */
    private static void addEvent(String input, List<Task> tasks) {
        String eventInput = input.substring(EVENT_PREFIX.length());
        int fromIndex = eventInput.indexOf(FROM_MARKER);
        int toIndex = eventInput.indexOf(TO_MARKER);
        if (fromIndex <= 0 || toIndex <= fromIndex + FROM_MARKER.length()) {
            System.out.println(EVENT_USAGE);
            return;
        }
        String event = eventInput.substring(0, fromIndex).trim();
        String from = eventInput.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
        String till = eventInput.substring(toIndex + TO_MARKER.length()).trim();
        if (event.isEmpty() || from.isEmpty() || till.isEmpty()) {
            System.out.println(EVENT_USAGE);
            return;
        }
        try {
            Task newTask = new Event(event, from, till);
            System.out.println("New objective: " + event
                    + "( from: " + from + " to: " + till + " )");
            tasks.add(newTask);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Checks whether an input begins a command that creates a task.
     *
     * @param input The command entered by the user.
     * @return {@code true} if the command creates a task.
     */
    private static boolean isTaskCreationCommand(String input) {
        return input.startsWith(TODO_PREFIX)
                || input.startsWith(DEADLINE_PREFIX)
                || input.startsWith(EVENT_PREFIX);
    }
}
