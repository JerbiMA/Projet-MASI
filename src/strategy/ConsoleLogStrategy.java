package strategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Strategie de journalisation dans la console.
 */
public class ConsoleLogStrategy implements LogStrategy {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void log(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println("[" + timestamp + "] " + message);
    }
}
