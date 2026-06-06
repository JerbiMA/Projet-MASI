package memento;

import model.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * Design Pattern Memento.
 * Capture l'etat complet d'un dessin (liste de formes) pour pouvoir le restaurer.
 */
public class DrawingMemento {

    private final List<Shape> shapes;

    public DrawingMemento(List<Shape> shapes) {
        // Copie profonde pour preserver l'etat
        this.shapes = new ArrayList<>();
        for (Shape shape : shapes) {
            this.shapes.add(shape.copy());
        }
    }

    public List<Shape> getShapes() {
        return shapes;
    }
}
