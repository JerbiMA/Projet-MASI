package model;

import memento.DrawingMemento;
import java.util.ArrayList;
import java.util.List;

/**
 * Represente un dessin contenant une liste de formes.
 * Utilise le pattern Memento pour sauvegarder/restaurer l'etat.
 */
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

    /** Memento: creer un snapshot de l'etat actuel */
    public DrawingMemento createMemento() {
        return new DrawingMemento(shapes);
    }

    /** Memento: restaurer l'etat a partir d'un snapshot */
    public void restoreMemento(DrawingMemento memento) {
        shapes.clear();
        for (Shape shape : memento.getShapes()) {
            shapes.add(shape.copy());
        }
    }
}
