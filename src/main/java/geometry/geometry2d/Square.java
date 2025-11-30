package geometry.geometry2d;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Square extends Figure {
    private double side;

    public Square(double x, double y, double side, Color color) {
        super(x, y, color);
        this.side = side;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x - side/2, y - side/2, side, side);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x - side/2, y - side/2, side, side);
    }

    @Override
    public boolean contains(double pointX, double pointY) {
        return pointX >= x - side/2 && pointX <= x + side/2 &&
                pointY >= y - side/2 && pointY <= y + side/2;
    }

    public double getSide() {
        return side;
    }
}