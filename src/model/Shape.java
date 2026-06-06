package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe abstraite representant une forme geometrique.
 */
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

    /** Dessiner le contour de la forme */
    public abstract void draw(GraphicsContext gc);

    /** Remplir l'interieur de la forme */
    public abstract void fillShape(GraphicsContext gc);

    /** Retourner le type de la forme (ex: "rectangle", "circle", "line") */
    public abstract String getType();

    /** Creer une copie de la forme */
    public abstract Shape copy();

    /** Verifier si un point (px, py) est a l'interieur de la forme */
    public abstract boolean contains(double px, double py);

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Color getStrokeColor() { return strokeColor; }

    // Methodes pour le pattern Decorator (valeurs par defaut)
    public Color getFillColor() { return null; }
    public boolean isDashed() { return false; }
}
