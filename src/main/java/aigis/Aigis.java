package aigis;

import java.util.Scanner;

/**
 * A simple command-line task manager.
 */
public class Aigis {
    /** Maximum number of tasks that Aigis can store. */
    private static final int MAX_TASKS = 100;

    private static final String BANNER = "     A       IIIII   GGGG    IIIII   SSSSS\n"
            + "    A A        I     G        I     S    \n"
            + "   AAAAA       I     G  GG    I      SSSS\n"
            + "  A     A      I     G   G    I          S\n"
            + " A       A   IIIII   GGGG   IIIII   SSSSS\n"
            + "\n"
            + "Aigis is ready to help!\n";

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
                } else {
                    System.out.println("New objective: " + input);
                    tasks[taskCount] = new Task(input);
                    taskCount++;
                }
            }
        }

        System.out.println(CLOSING);
    }
}
