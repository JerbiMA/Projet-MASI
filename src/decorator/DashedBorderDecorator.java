package decorator;

import javafx.scene.canvas.GraphicsContext;
import model.Shape;

/**
 * Decorateur qui ajoute une bordure en tirets a une forme.
 */
public class DashedBorderDecorator extends ShapeDecorator {

    public DashedBorderDecorator(Shape shape) {
        super(shape);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setLineDashes(10, 5);
        decoratedShape.draw(gc);
        gc.restore();
    }

    @Override
    public boolean isDashed() {
        return true;
    }

    @Override
    public Shape copy() {
        return new DashedBorderDecorator(decoratedShape.copy());
    }
}
