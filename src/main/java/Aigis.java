import java.util.Scanner;

/**
 * A simple command-line task manager.
 */
public class Aigis {
    public static void main(String[] args) {
        String banner = "     A       IIIII   GGGG    IIIII   SSSSS\n"
                + "    A A        I     G        I     S    \n"
                + "   AAAAA       I     G  GG    I      SSSS\n"
                + "  A     A      I     G   G    I          S\n"
                + " A       A   IIIII   GGGG   IIIII   SSSSS\n"
                + "\n"
                + "Aigis is ready to help!\n";
        String closing = " ----------------------------------------------------\n"
                + "Tasks completed. See you again soon!\n"
                + "__________________________________________________________\n";
        System.out.println(banner);

        int taskCount = 0;
        String[] taskList = new String[100];
        boolean[] completedTasks = new boolean[100];
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("list")){ // Command to view current tasks
                for (int i = 0; i < taskCount; i++) {
                    String status = completedTasks[i] ? "[X]" : "[ ]";
                    System.out.println(i + 1 + ". " + status + " " + taskList[i]);
                }
            }
            else if (input.equals("bye")) { // Command to end the program
                break;
            }
            else if (input.startsWith("mark ")) { // Command to mark a task as done
                String taskNumber = input.substring(5).trim();
                try {
                    int taskDone = Integer.parseInt(taskNumber) - 1;
                    if (taskDone >= 0 && taskDone < taskCount) {
                        completedTasks[taskDone] = true;
                        System.out.println("Marked as done: " + taskList[taskDone]);
                    } else {
                        System.out.println("That task does not exist.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid task number.");
                }
            }
            else if (input.startsWith("unmark ")) { // Command to mark a task as done
                String taskNumber = input.substring(7).trim();
                try {
                    int taskDone = Integer.parseInt(taskNumber) - 1;
                    if (taskDone >= 0 && taskDone < taskCount) {
                        completedTasks[taskDone] = false;
                        System.out.println("Unmarked as done: " + taskList[taskDone]);
                    } else {
                        System.out.println("That task does not exist.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid task number.");
                }
            }
            else {
                System.out.println("New objective: " + input);
                taskList[taskCount] = input;
                taskCount++;
            }
        }

        System.out.println(closing);
    }
}
