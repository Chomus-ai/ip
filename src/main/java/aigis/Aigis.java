package aigis;

import aigis.task.Task;

/**
 * A simple command-line task manager.
 */
public class Aigis {
    /**
     * Starts the Aigis command-line task manager.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Storage storage = new Storage("data/aigis.txt");
        try (Ui ui = new Ui()) {
            ui.showWelcome();

            Task[] savedTasks = new Task[TaskList.MAX_TASKS];
            int savedTaskCount = storage.load(savedTasks);
            TaskList tasks = new TaskList(savedTasks, savedTaskCount);
            Parser parser = new Parser();

            try {
                runCommandLoop(tasks, ui, parser);
            } finally {
                storage.save(tasks.toArray(), tasks.size());
            }
            ui.showGoodbye();
        }
    }

    /**
     * Reads and processes commands until the user exits or input is exhausted.
     *
     * @param tasks The task storage used by the command loop.
     * @param ui The UI used for console input and output.
     * @param parser The parser used to interpret user commands.
     */
    private static void runCommandLoop(TaskList tasks, Ui ui, Parser parser) {
        String input;
        while ((input = ui.readCommand()) != null) {
            Parser.Command command = parser.parse(input);
            if (command.getType() == Parser.CommandType.EXIT) {
                break;
            }
            processCommand(command, tasks, ui);
        }
    }

    /**
     * Executes one parsed command.
     *
     * @param command The parsed command to execute.
     * @param tasks The task storage used by the command.
     * @param ui The UI used to display command results.
     */
    private static void processCommand(Parser.Command command, TaskList tasks, Ui ui) {
        switch (command.getType()) {
        case LIST:
            ui.showTasks(tasks);
            break;
        case MARK:
        case UNMARK:
            updateTaskStatus(command, tasks, ui);
            break;
        case DELETE:
            deleteTask(command, tasks, ui);
            break;
        case ADD:
            addTask(command, tasks, ui);
            break;
        case INVALID:
        case UNKNOWN:
            ui.showMessage(command.getMessage());
            break;
        default:
            break;
        }
    }

    /**
     * Updates a task's completion status based on a mark or unmark command.
     *
     * @param command The parsed status command.
     * @param tasks The task storage containing the target task.
     * @param ui The UI used to display the result.
     */
    private static void updateTaskStatus(Parser.Command command, TaskList tasks, Ui ui) {
        int taskIndex = command.getTaskNumber() - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            ui.showMessage("That task does not exist.");
            return;
        }
        boolean isDone = command.getType() == Parser.CommandType.MARK;
        tasks.get(taskIndex).setDone(isDone);
        String statusMessage = isDone ? "Marked as done: " : "Unmarked as done: ";
        ui.showMessage(statusMessage + tasks.get(taskIndex));
    }

    /**
     * Deletes a task selected by its one-based display number.
     *
     * @param command The parsed delete command.
     * @param tasks The task storage from which to remove the target task.
     * @param ui The UI used to display the result.
     */
    private static void deleteTask(Parser.Command command, TaskList tasks, Ui ui) {
        int taskIndex = command.getTaskNumber() - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            ui.showMessage("That task does not exist.");
            return;
        }
        Task deletedTask = tasks.remove(taskIndex);
        ui.showMessage("Deleted task: " + deletedTask);
    }

    /**
     * Adds a task represented by a parsed add command.
     *
     * @param command The parsed add command.
     * @param tasks The task storage receiving the new task.
     * @param ui The UI used to display the result.
     */
    private static void addTask(Parser.Command command, TaskList tasks, Ui ui) {
        if (tasks.isFull()) {
            ui.showMessage("The task list is full.");
            return;
        }
        if (command.getTask() == null) {
            ui.showMessage(command.getMessage());
            return;
        }
        ui.showMessage(command.getMessage());
        tasks.add(command.getTask());
    }
}
