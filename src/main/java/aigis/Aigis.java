package aigis;

import java.util.Scanner;

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
              "      __        __     _______   __      ________  \n" +
              "     /\"\"\\      |\" \\   /\" _   \"| |\" \\    /\"       ) \n" +
              "    /    \\     ||  | (: ( \\___) ||  |  (:   \\___/  \n" +
              "   /' /\\  \\    |:  |  \\/ \\      |:  |   \\___  \\    \n" +
              "  //  __'  \\   |.  |  //  \\ ___ |.  |    __/  \\\\   \n" +
              " /   /  \\\\  \\  /\\  |\\(:   _(  _|/\\  |\\  /\" \\   :)  \n" +
              "(___/    \\___)(__\\_|_)\\_______)(__\\_|_)(_______/   \n"
            + "\n"
            + "Aigis is ready to help!\n"
            + "Awaiting commands...\n";

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

        Task[] tasks = new Task[MAX_TASKS];
        runCommandLoop(tasks);

        System.out.println(CLOSING);
    }

    /**
     * Reads and processes commands until the user exits or input is exhausted.
     *
     * @param tasks The task storage used by the command loop.
     */
    private static void runCommandLoop(Task[] tasks) {
        int taskCount = 0;
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();

                if (input.equals(BYE_COMMAND)) {
                    break;
                }
                taskCount = processCommand(input, tasks, taskCount);
            }
        }
    }

    /**
     * Processes one non-exit command and returns the updated task count.
     *
     * @param input The command entered by the user.
     * @param tasks The task storage used by the command.
     * @param taskCount The number of tasks currently stored.
     * @return The updated number of stored tasks.
     */
    private static int processCommand(String input, Task[] tasks, int taskCount) {
        if (input.equals(LIST_COMMAND)) {
            printTasks(tasks, taskCount);
            return taskCount;
        }
        if (input.startsWith(MARK_PREFIX)) {
            updateTaskStatus(input, tasks, taskCount, MARK_PREFIX, true);
            return taskCount;
        }
        if (input.startsWith(UNMARK_PREFIX)) {
            updateTaskStatus(input, tasks, taskCount, UNMARK_PREFIX, false);
            return taskCount;
        }
        if (isTaskCreationCommand(input) && taskCount >= tasks.length) {
            System.out.println("The task list is full.");
            return taskCount;
        }
        if (input.startsWith(TODO_PREFIX)) {
            return addTodo(input, tasks, taskCount);
        }
        if (input.startsWith(DEADLINE_PREFIX)) {
            return addDeadline(input, tasks, taskCount);
        }
        if (input.startsWith(EVENT_PREFIX)) {
            return addEvent(input, tasks, taskCount);
        }
        System.out.println(UNKNOWN_COMMAND_MESSAGE);
        return taskCount;
    }

    /**
     * Prints every stored task with its one-based display number.
     *
     * @param tasks The task storage to display.
     * @param taskCount The number of tasks to display.
     */
    private static void printTasks(Task[] tasks, int taskCount) {
        for (int i = 0; i < taskCount; i++) {
            System.out.println(i + 1 + ". " + tasks[i]);
        }
    }

    /**
     * Updates a task's completion status based on a mark or unmark command.
     *
     * @param input The status command entered by the user.
     * @param tasks The task storage containing the target task.
     * @param taskCount The number of tasks currently stored.
     * @param commandPrefix The prefix to remove before parsing the task number.
     * @param isDone The completion status to assign.
     */
    private static void updateTaskStatus(String input, Task[] tasks, int taskCount,
                                         String commandPrefix, boolean isDone) {
        String taskNumber = input.substring(commandPrefix.length()).trim();
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                System.out.println("That task does not exist.");
                return;
            }
            tasks[taskIndex].setDone(isDone);
            String statusMessage = isDone ? "Marked as done: " : "Unmarked as done: ";
            System.out.println(statusMessage + tasks[taskIndex]);
        } catch (NumberFormatException exception) {
            System.out.println("Please provide a valid task number.");
        }
    }

    /**
     * Adds a todo task if its description is non-empty.
     *
     * @param input The todo command entered by the user.
     * @param tasks The task storage receiving the new task.
     * @param taskCount The number of tasks currently stored.
     * @return The updated number of stored tasks.
     */
    private static int addTodo(String input, Task[] tasks, int taskCount) {
        String todo = input.substring(TODO_PREFIX.length()).trim();
        if (todo.isEmpty()) {
            System.out.println("Please provide a task description.");
            return taskCount;
        }
        System.out.println("New objective: " + todo);
        tasks[taskCount] = new Todo(todo);
        return taskCount + 1;
    }

    /**
     * Adds a deadline task when both its description and due date are present.
     *
     * @param input The deadline command entered by the user.
     * @param tasks The task storage receiving the new task.
     * @param taskCount The number of tasks currently stored.
     * @return The updated number of stored tasks.
     */
    private static int addDeadline(String input, Task[] tasks, int taskCount) {
        String deadlineInput = input.substring(DEADLINE_PREFIX.length());
        int byIndex = deadlineInput.indexOf(BY_MARKER);
        if (byIndex <= 0) {
            System.out.println(DEADLINE_USAGE);
            return taskCount;
        }
        String deadline = deadlineInput.substring(0, byIndex).trim();
        String due = deadlineInput.substring(byIndex + BY_MARKER.length()).trim();
        if (deadline.isEmpty() || due.isEmpty()) {
            System.out.println(DEADLINE_USAGE);
            return taskCount;
        }
        System.out.println("New objective: " + deadline + " ( by: " + due + " )");
        tasks[taskCount] = new Deadline(deadline, due);
        return taskCount + 1;
    }

    /**
     * Adds an event task when its description, start time, and end time are present.
     *
     * @param input The event command entered by the user.
     * @param tasks The task storage receiving the new task.
     * @param taskCount The number of tasks currently stored.
     * @return The updated number of stored tasks.
     */
    private static int addEvent(String input, Task[] tasks, int taskCount) {
        String eventInput = input.substring(EVENT_PREFIX.length());
        int fromIndex = eventInput.indexOf(FROM_MARKER);
        int toIndex = eventInput.indexOf(TO_MARKER);
        if (fromIndex <= 0 || toIndex <= fromIndex + FROM_MARKER.length()) {
            System.out.println(EVENT_USAGE);
            return taskCount;
        }
        String event = eventInput.substring(0, fromIndex).trim();
        String from = eventInput.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
        String till = eventInput.substring(toIndex + TO_MARKER.length()).trim();
        if (event.isEmpty() || from.isEmpty() || till.isEmpty()) {
            System.out.println(EVENT_USAGE);
            return taskCount;
        }
        System.out.println("New objective: " + event
                + "( from: " + from + " to: " + till + " )");
        tasks[taskCount] = new Event(event, from, till);
        return taskCount + 1;
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
