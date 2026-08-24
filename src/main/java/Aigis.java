import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("list")){ // Command to view current tasks
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(i+1 + ". " + taskList[i]);
                }
            }
            else if (input.equals("bye")) { // Command to end the program
                break;
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
