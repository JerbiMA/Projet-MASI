package command;

import model.Drawing;
import model.Shape;

public class DeleteCommand implements Command {

    private final Drawing drawing;
    private final Shape shape;

    public DeleteCommand(Drawing drawing, Shape shape) {
        this.drawing = drawing;
        this.shape = shape;
    }

    @Override
    public void execute() {
        drawing.removeShape(shape);
    }

    @Override
    public void undo() {
        drawing.addShape(shape);
    }

    @Override
    public String getDescription() {
        return "Suppression d'un " + shape.getType();
    }
}
