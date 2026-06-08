package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Shape {

    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected Color strokeColor;

    public Shape(double x, double y, double width, double height, Color strokeColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.strokeColor = strokeColor;
    }

    public abstract void draw(GraphicsContext gc);

    public abstract void fillShape(GraphicsContext gc);

    public abstract String getType();

    public abstract Shape copy();

    public abstract boolean contains(double px, double py);

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Color getStrokeColor() { return strokeColor; }

    public Color getFillColor() { return null; }
    public boolean isDashed() { return false; }
}
