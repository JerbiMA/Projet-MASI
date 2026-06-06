package command;

import java.util.Stack;

/**
 * Historique des commandes executees (pour le Undo).
 */
public class CommandHistory {

    private final Stack<Command> history = new Stack<>();

    /** Executer une commande et l'ajouter a l'historique */
    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
    }

    /** Annuler la derniere commande */
    public Command undo() {
        Command command = history.pop();
        command.undo();
        return command;
    }

    /** Verifier si on peut annuler */
    public boolean canUndo() {
        return !history.isEmpty();
    }

    /** Vider l'historique */
    public void clear() {
        history.clear();
    }
}
