package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleShape extends Shape {

    public CircleShape(double startX, double startY, double endX, double endY, Color color) {
        super(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY), color);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(strokeColor);
        gc.setLineWidth(2);
        gc.strokeOval(x, y, width, height);
    }

    @Override
    public void fillShape(GraphicsContext gc) {
        gc.fillOval(x, y, width, height);
    }

    @Override
    public String getType() {
        return "circle";
    }

    @Override
    public Shape copy() {
        return new CircleShape(x, y, x + width, y + height, strokeColor);
    }

    @Override
    public boolean contains(double px, double py) {
        double cx = x + width / 2;
        double cy = y + height / 2;
        double rx = width / 2;
        double ry = height / 2;
        double normalized = Math.pow(px - cx, 2) / Math.pow(rx, 2) + Math.pow(py - cy, 2) / Math.pow(ry, 2);
        return normalized <= 1;
    }
}
