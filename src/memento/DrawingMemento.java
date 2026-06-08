package memento;

import model.Shape;
import java.util.ArrayList;
import java.util.List;

public class DrawingMemento {

    private final List<Shape> shapes;

    public DrawingMemento(List<Shape> shapes) {
        this.shapes = new ArrayList<>();
        for (Shape shape : shapes) {
            this.shapes.add(shape.copy());
        }
    }

    public List<Shape> getShapes() {
        return shapes;
    }
}
