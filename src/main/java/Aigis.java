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

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            System.out.println(command);
            if (command.equals("bye")) {
                break;
            }
        }

        System.out.println(closing);
    }
}
