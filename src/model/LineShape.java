package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LineShape extends Shape {

    public LineShape(double startX, double startY, double endX, double endY, Color color) {
        super(startX, startY, endX - startX, endY - startY, color);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(strokeColor);
        gc.setLineWidth(2);
        gc.strokeLine(x, y, x + width, y + height);
    }

    @Override
    public void fillShape(GraphicsContext gc) {
    }

    @Override
    public String getType() {
        return "line";
    }

    @Override
    public Shape copy() {
        return new LineShape(x, y, x + width, y + height, strokeColor);
    }

    @Override
    public boolean contains(double px, double py) {
        double endX = x + width;
        double endY = y + height;
        double length = Math.sqrt(Math.pow(endX - x, 2) + Math.pow(endY - y, 2));
        double dist = Math.abs((endY - y) * px - (endX - x) * py + endX * y - endY * x) / length;
        double minX = Math.min(x, endX) - 5;
        double maxX = Math.max(x, endX) + 5;
        double minY = Math.min(y, endY) - 5;
        double maxY = Math.max(y, endY) + 5;
        return dist < 5 && px >= minX && px <= maxX && py >= minY && py <= maxY;
    }
}
