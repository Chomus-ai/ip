package aigis;

import java.util.Scanner;

/**
 * A simple command-line task manager.
 */
public class Aigis {
    /** Maximum number of tasks that Aigis can store. */
    private static final int MAX_TASKS = 100;

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

        int taskCount = 0;
        Task[] tasks = new Task[MAX_TASKS];

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();

                if (input.equals("list")) {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(i + 1 + ". " + tasks[i]);
                    }
                } else if (input.equals("bye")) {
                    break;
                } else if (input.startsWith("mark ")) {
                    String taskNumber = input.substring(5).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex >= 0 && taskIndex < taskCount) {
                            tasks[taskIndex].setDone(true);
                            System.out.println("Marked as done: " + tasks[taskIndex]);
                        } else {
                            System.out.println("That task does not exist.");
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (input.startsWith("unmark ")) {
                    String taskNumber = input.substring(7).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex >= 0 && taskIndex < taskCount) {
                            tasks[taskIndex].setDone(false);
                            System.out.println("Unmarked as done: " + tasks[taskIndex]);
                        } else {
                            System.out.println("That task does not exist.");
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (taskCount >= tasks.length) {
                    System.out.println("The task list is full.");
                } else if (input.startsWith("todo ")) {
                    String todo = input.substring(5).trim();
                    if (todo.isEmpty()) {
                        System.out.println("Please provide a task description.");
                        continue;
                    }
                    System.out.println("New objective: " + todo);
                    tasks[taskCount] = new Todo(todo);
                    taskCount++;
                } else if (input.startsWith("deadline ")) {
                    String deadlineInput = input.substring(9);
                    int byIndex = deadlineInput.indexOf("/by");
                    if (byIndex <= 0) {
                        System.out.println("Please use: deadline <description> /by <date>");
                        continue;
                    }
                    String deadline = deadlineInput.substring(0, byIndex).trim();
                    String due = deadlineInput.substring(byIndex + 3).trim();
                    if (deadline.isEmpty() || due.isEmpty()) {
                        System.out.println("Please use: deadline <description> /by <date>");
                        continue;
                    }
                    System.out.println("New objective: " + deadline + " ( by: " + due + " )");
                    tasks[taskCount] = new Deadline(deadline, due);
                    taskCount++;
                } else if (input.startsWith("event ")) {
                    String eventInput = input.substring(6);
                    int fromIndex = eventInput.indexOf("/from");
                    int toIndex = eventInput.indexOf("/to");
                    if (fromIndex <= 0 || toIndex <= fromIndex + 5) {
                        System.out.println("Please use: event <description> /from <start> /to <end>");
                        continue;
                    }
                    String event = eventInput.substring(0, fromIndex).trim();
                    String from = eventInput.substring(fromIndex + 5, toIndex).trim();
                    String till = eventInput.substring(toIndex + 3).trim();
                    if (event.isEmpty() || from.isEmpty() || till.isEmpty()) {
                        System.out.println("Please use: event <description> /from <start> /to <end>");
                        continue;
                    }
                    System.out.println("New objective: " + event + "( from: " + from + " to: " + till + " )");
                    tasks[taskCount] = new Event(event, from, till);
                    taskCount++;
                }
            }
        }

        System.out.println(CLOSING);
    }
}
