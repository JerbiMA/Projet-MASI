package decorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.Shape;

/**
 * Design Pattern Decorator - Classe abstraite.
 * Enveloppe une forme pour lui ajouter des fonctionnalites visuelles.
 */
public abstract class ShapeDecorator extends Shape {

    protected Shape decoratedShape;

    public ShapeDecorator(Shape shape) {
        super(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight(), shape.getStrokeColor());
        this.decoratedShape = shape;
    }

    @Override
    public void draw(GraphicsContext gc) {
        decoratedShape.draw(gc);
    }

    @Override
    public void fillShape(GraphicsContext gc) {
        decoratedShape.fillShape(gc);
    }

    @Override
    public String getType() {
        return decoratedShape.getType();
    }

    @Override
    public boolean contains(double px, double py) {
        return decoratedShape.contains(px, py);
    }

    // Deleguer les getters a la forme decoree
    @Override
    public double getX() { return decoratedShape.getX(); }

    @Override
    public double getY() { return decoratedShape.getY(); }

    @Override
    public double getWidth() { return decoratedShape.getWidth(); }

    @Override
    public double getHeight() { return decoratedShape.getHeight(); }

    @Override
    public Color getStrokeColor() { return decoratedShape.getStrokeColor(); }

    @Override
    public Color getFillColor() { return decoratedShape.getFillColor(); }

    @Override
    public boolean isDashed() { return decoratedShape.isDashed(); }
}
