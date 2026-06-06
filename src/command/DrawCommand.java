package command;

import model.Drawing;
import model.Shape;

/**
 * Commande pour ajouter une forme au dessin.
 */
public class DrawCommand implements Command {

    private final Drawing drawing;
    private final Shape shape;

    public DrawCommand(Drawing drawing, Shape shape) {
        this.drawing = drawing;
        this.shape = shape;
    }

    @Override
    public void execute() {
        drawing.addShape(shape);
    }

    @Override
    public void undo() {
        drawing.removeShape(shape);
    }

    @Override
    public String getDescription() {
        return "Ajout d'un " + shape.getType();
    }
}
