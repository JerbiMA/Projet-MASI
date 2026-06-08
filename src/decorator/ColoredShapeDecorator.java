package decorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.Shape;

public class ColoredShapeDecorator extends ShapeDecorator {

    private final Color fillColor;

    public ColoredShapeDecorator(Shape shape, Color fillColor) {
        super(shape);
        this.fillColor = fillColor;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFill(fillColor);
        gc.setGlobalAlpha(0.3);
        decoratedShape.fillShape(gc);
        gc.restore();
        decoratedShape.draw(gc);
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public Shape copy() {
        return new ColoredShapeDecorator(decoratedShape.copy(), fillColor);
    }
}
