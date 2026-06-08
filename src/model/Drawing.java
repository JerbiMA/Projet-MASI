package model;

import memento.DrawingMemento;
import java.util.ArrayList;
import java.util.List;

public class Drawing {

    private List<Shape> shapes = new ArrayList<>();

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void clear() {
        shapes.clear();
    }

    public DrawingMemento createMemento() {
        return new DrawingMemento(shapes);
    }

    public void restoreMemento(DrawingMemento memento) {
        shapes.clear();
        for (Shape shape : memento.getShapes()) {
            shapes.add(shape.copy());
        }
    }
}
