package singleton;

import observer.ActionObserver;
import strategy.ConsoleLogStrategy;
import strategy.LogStrategy;

public class Logger implements ActionObserver {

    private static final Logger INSTANCE = new Logger();

    private LogStrategy strategy;

    private Logger() {
        this.strategy = new ConsoleLogStrategy();
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

    public void setStrategy(LogStrategy strategy) {
        this.strategy = strategy;
    }

    public LogStrategy getStrategy() {
        return strategy;
    }

    public void log(String message) {
        strategy.log(message);
    }

    @Override
    public void onAction(String action) {
        log(action);
    }
}
