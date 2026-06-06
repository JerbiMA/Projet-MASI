package strategy;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Strategie de journalisation dans une base de donnees SQLite.
 */
public class DatabaseLogStrategy implements LogStrategy {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Connection connection;

    public DatabaseLogStrategy(Connection connection) {
        this.connection = connection;
        initTable();
    }

    private void initTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS logs ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "timestamp TEXT, "
                + "message TEXT)");
        } catch (SQLException e) {
            System.err.println("Erreur initialisation table logs: " + e.getMessage());
        }
    }

    @Override
    public void log(String message) {
        String sql = "INSERT INTO logs (timestamp, message) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, LocalDateTime.now().format(FORMATTER));
            ps.setString(2, message);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur ecriture log en BDD: " + e.getMessage());
        }
    }
}
