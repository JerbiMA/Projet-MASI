package strategy;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogStrategy implements LogStrategy {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String filename;

    public FileLogStrategy(String filename) {
        this.filename = filename;
    }

    @Override
    public void log(String message) {
        try (FileWriter fw = new FileWriter(filename, true); PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            pw.println("[" + timestamp + "] " + message);
        } catch (IOException e) {
            System.err.println("Erreur d'ecriture dans le fichier log: " + e.getMessage());
        }
    }
}
