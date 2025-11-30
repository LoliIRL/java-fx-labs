package geometry.geometry2d;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle extends Figure {
    private double size;

    public Triangle(double x, double y, double size, Color color) {
        super(x, y, color);
        this.size = size;
    }

    @Override
    public void draw(GraphicsContext gc) {
        double[] xPoints = {x, x - size/2, x + size/2};
        double[] yPoints = {y - size/2, y + size/2, y + size/2};

        gc.setFill(color);
        gc.fillPolygon(xPoints, yPoints, 3);
        gc.setStroke(Color.BLACK);
        gc.strokePolygon(xPoints, yPoints, 3);
    }

    @Override
    public boolean contains(double pointX, double pointY) {
        return pointX >= x - size/2 && pointX <= x + size/2 &&
                pointY >= y - size/2 && pointY <= y + size/2;
    }

    public double getSize() {
        return size;
    }
}