package aigis;

import aigis.task.Task;

/**
 * Represents a command that adds a parsed task to the task list.
 */
public final class AddCommand extends Command {
    private final Task task;
    private final String message;

    /**
     * Creates an add command.
     *
     * @param task The parsed task, or {@code null} when parsing failed.
     * @param message The success or validation message to display.
     */
    public AddCommand(Task task, String message) {
        this.task = task;
        this.message = message;
    }

    /**
     * Adds the task when the command is valid and capacity is available.
     *
     * @param tasks The task list receiving the task.
     * @param ui The UI used to display the result.
     * @param storage The storage service, which saves when the application exits.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (tasks.isFull()) {
            ui.showMessage("The task list is full.");
            return;
        }
        if (task == null) {
            ui.showMessage(message);
            return;
        }
        ui.showMessage(message);
        tasks.add(task);
    }
}
