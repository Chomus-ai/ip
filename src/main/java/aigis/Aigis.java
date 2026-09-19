package aigis;

import aigis.task.Task;

/**
 * A simple command-line task manager.
 */
public class Aigis implements AutoCloseable {
    private static final String DEFAULT_FILE_PATH = "data/aigis.txt";

    /** The persistence component used by the application. */
    private final Storage storage;

    /** The task collection used by the application. */
    private final TaskList tasks;

    /** The console interaction component used by the application. */
    private final Ui ui;

    /** The command parser used by the application. */
    private final Parser parser;

    /**
     * Creates an Aigis application using the specified data file.
     *
     * @param filePath The path of the file used to load and save tasks.
     */
    public Aigis(String filePath) {
        storage = new Storage(filePath);
        ui = new Ui();
        tasks = storage.load();
        parser = new Parser();
    }

    /**
     * Runs the command-line task manager until the user exits or input ends.
     */
    public void run() {
        ui.showWelcome();
        try {
            runCommandLoop();
        } finally {
            storage.save(tasks);
        }
        ui.showGoodbye();
    }

    /**
     * Starts the Aigis command-line task manager.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        try (Aigis aigis = new Aigis(DEFAULT_FILE_PATH)) {
            aigis.run();
        }
    }

    /**
     * Closes the application's console input.
     */
    @Override
    public void close() {
        ui.close();
    }

    /**
     * Reads and processes commands until the user exits or input is exhausted.
     *
     */
    private void runCommandLoop() {
        String input;
        while ((input = ui.readCommand()) != null) {
            Parser.Command command = parser.parse(input);
            if (command.getType() == Parser.CommandType.EXIT) {
                break;
            }
            processCommand(command);
        }
    }

    /**
     * Executes one parsed command.
     *
     * @param command The parsed command to execute.
     */
    private void processCommand(Parser.Command command) {
        switch (command.getType()) {
        case LIST:
            ui.showTasks(tasks);
            break;
        case MARK:
        case UNMARK:
            updateTaskStatus(command);
            break;
        case DELETE:
            deleteTask(command);
            break;
        case ADD:
            addTask(command);
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
     */
    private void updateTaskStatus(Parser.Command command) {
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
     */
    private void deleteTask(Parser.Command command) {
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
     */
    private void addTask(Parser.Command command) {
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
