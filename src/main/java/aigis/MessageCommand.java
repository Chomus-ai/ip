package aigis;

/**
 * Represents a command that displays a parser or validation message.
 */
public final class MessageCommand extends Command {
    private final String message;

    /**
     * Creates a message command.
     *
     * @param message The message to display.
     */
    public MessageCommand(String message) {
        this.message = message;
    }

    /**
     * Displays the stored message.
     *
     * @param tasks The task list, which is unchanged.
     * @param ui The UI used to display the message.
     * @param storage The storage service, which is not needed for messages.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage(message);
    }
}
