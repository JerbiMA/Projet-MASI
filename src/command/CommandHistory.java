package command;

import java.util.Stack;

public class CommandHistory {

    private final Stack<Command> history = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
    }

    public Command undo() {
        Command command = history.pop();
        command.undo();
        return command;
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public void clear() {
        history.clear();
    }
}
