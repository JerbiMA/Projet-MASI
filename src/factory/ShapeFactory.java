package factory;

import model.*;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class ShapeFactory {

    @FunctionalInterface
    public interface ShapeCreator {
        Shape create(double startX, double startY, double endX, double endY, Color color);
    }

    private final Map<String, ShapeCreator> creators = new HashMap<>();

    public ShapeFactory() {
        creators.put("rectangle", RectangleShape::new);
        creators.put("circle", CircleShape::new);
        creators.put("line", LineShape::new);
    }

    public Shape createShape(String type, double startX, double startY, double endX, double endY, Color color) {
        ShapeCreator creator = creators.get(type);
        return creator.create(startX, startY, endX, endY, color);
    }
}
