package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Forme rectangle.
 */
public class RectangleShape extends Shape {

    public RectangleShape(double startX, double startY, double endX, double endY, Color color) {
        super(Math.min(startX, endX), Math.min(startY, endY),
              Math.abs(endX - startX), Math.abs(endY - startY), color);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(strokeColor);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, width, height);
    }

    @Override
    public void fillShape(GraphicsContext gc) {
        gc.fillRect(x, y, width, height);
    }

    @Override
    public String getType() {
        return "rectangle";
    }

    @Override
    public Shape copy() {
        return new RectangleShape(x, y, x + width, y + height, strokeColor);
    }

    @Override
    public boolean contains(double px, double py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
}
