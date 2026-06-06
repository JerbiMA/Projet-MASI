package singleton;

import observer.ActionObserver;
import strategy.ConsoleLogStrategy;
import strategy.LogStrategy;

/**
 * Design Pattern Singleton.
 * Logger unique pour toute l'application.
 * Implemente ActionObserver pour recevoir les notifications d'actions.
 */
public class Logger implements ActionObserver {

    // Initialisation immediate (eager) - Singleton
    private static final Logger INSTANCE = new Logger();

    private LogStrategy strategy;

    private Logger() {
        this.strategy = new ConsoleLogStrategy();
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

    /** Changer la strategie de journalisation */
    public void setStrategy(LogStrategy strategy) {
        this.strategy = strategy;
    }

    public LogStrategy getStrategy() {
        return strategy;
    }

    /** Journaliser un message */
    public void log(String message) {
        strategy.log(message);
    }

    /** Observer: appele automatiquement quand une action est effectuee */
    @Override
    public void onAction(String action) {
        log(action);
    }
}
