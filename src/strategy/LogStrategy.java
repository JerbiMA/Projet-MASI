package strategy;

/**
 * Interface Strategy (Design Pattern Strategie).
 * Definit la strategie de journalisation.
 */
public interface LogStrategy {

    /** Journaliser un message */
    void log(String message);
}
