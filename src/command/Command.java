package command;

/**
 * Interface Command (Design Pattern Command).
 * Chaque action de l'utilisateur est encapsulee dans un objet Command.
 */
public interface Command {

    /** Executer la commande */
    void execute();

    /** Annuler la commande */
    void undo();

    /** Description de la commande pour le journal */
    String getDescription();
}
