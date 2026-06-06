package factory;

import model.*;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Design Pattern Factory.
 * Cree des formes geometriques a partir d'un type (String).
 * Utilise un registre (Map) au lieu de if/else.
 */
public class ShapeFactory {

    @FunctionalInterface
    public interface ShapeCreator {
        Shape create(double startX, double startY, double endX, double endY, Color color);
    }

    private final Map<String, ShapeCreator> creators = new HashMap<>();

    public ShapeFactory() {
        // Enregistrer les formes disponibles
        creators.put("rectangle", RectangleShape::new);
        creators.put("circle", CircleShape::new);
        creators.put("line", LineShape::new);
    }

    /**
     * Creer une forme a partir de son type et ses coordonnees.
     */
    public Shape createShape(String type, double startX, double startY,
                              double endX, double endY, Color color) {
        ShapeCreator creator = creators.get(type);
        return creator.create(startX, startY, endX, endY, color);
    }
}
