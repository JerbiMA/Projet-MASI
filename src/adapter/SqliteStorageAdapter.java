package adapter;

import decorator.ColoredShapeDecorator;
import decorator.DashedBorderDecorator;
import factory.ShapeFactory;
import memento.DrawingMemento;
import model.Shape;
import javafx.scene.paint.Color;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqliteStorageAdapter implements StorageAdapter {

    private Connection connection;
    private final ShapeFactory shapeFactory;

    public SqliteStorageAdapter(String dbPath) {
        this.shapeFactory = new ShapeFactory();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            initTables();
        } catch (SQLException e) {
            System.err.println("Erreur connexion BDD: " + e.getMessage());
        }
    }

    private void initTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS drawings (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT UNIQUE, " + "created_at TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS shapes (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "drawing_id INTEGER, " + "type TEXT, " + "x REAL, y REAL, width REAL, height REAL, " + "stroke_color TEXT, " + "fill_color TEXT, " + "dashed INTEGER, " + "FOREIGN KEY(drawing_id) REFERENCES drawings(id))");
        } catch (SQLException e) {
            System.err.println("Erreur initialisation tables: " + e.getMessage());
        }
    }

    @Override
    public void saveDrawing(String name, DrawingMemento memento) {
        try {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM shapes WHERE drawing_id IN (SELECT id FROM drawings WHERE name = ?)") ) {
                ps.setString(1, name);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM drawings WHERE name = ?")) {
                ps.setString(1, name);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO drawings (name, created_at) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setString(2, LocalDateTime.now().toString());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                int drawingId = keys.getInt(1);

                for (Shape shape : memento.getShapes()) {
                    saveShape(drawingId, shape);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur sauvegarde: " + e.getMessage());
        }
    }

    private void saveShape(int drawingId, Shape shape) throws SQLException {
        String sql = "INSERT INTO shapes (drawing_id, type, x, y, width, height, " + "stroke_color, fill_color, dashed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, drawingId);
            ps.setString(2, shape.getType());
            ps.setDouble(3, shape.getX());
            ps.setDouble(4, shape.getY());
            ps.setDouble(5, shape.getWidth());
            ps.setDouble(6, shape.getHeight());
            ps.setString(7, colorToString(shape.getStrokeColor()));

            Color fill = shape.getFillColor();
            ps.setString(8, fill != null ? colorToString(fill) : null);
            ps.setInt(9, shape.isDashed() ? 1 : 0);

            ps.executeUpdate();
        }
    }

    @Override
    public DrawingMemento loadDrawing(String name) {
        List<Shape> shapes = new ArrayList<>();
        String sql = "SELECT s.* FROM shapes s " + "JOIN drawings d ON s.drawing_id = d.id WHERE d.name = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double w = rs.getDouble("width");
                double h = rs.getDouble("height");
                Color strokeColor = Color.web(rs.getString("stroke_color"));
                String fillColorStr = rs.getString("fill_color");
                boolean dashed = rs.getInt("dashed") == 1;

                Shape shape = shapeFactory.createShape(type, x, y, x + w, y + h, strokeColor);

                shape = fillColorStr != null ? new ColoredShapeDecorator(shape, Color.web(fillColorStr)) : shape;
                shape = dashed ? new DashedBorderDecorator(shape) : shape;

                shapes.add(shape);
            }
        } catch (SQLException e) {
            System.err.println("Erreur chargement: " + e.getMessage());
        }

        return new DrawingMemento(shapes);
    }

    @Override
    public List<String> listDrawings() {
        List<String> names = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT name FROM drawings ORDER BY created_at DESC")) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur listage: " + e.getMessage());
        }
        return names;
    }

    public Connection getConnection() {
        return connection;
    }

    private String colorToString(Color color) {
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }
}
