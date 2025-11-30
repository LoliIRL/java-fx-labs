package geometry;

import geometry.geometry2d.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeometryController {
    @FXML
    private Canvas canvas;
    @FXML
    private Pane canvasPane;

    private List<Figure> figures = new ArrayList<>();
    private Figure selectedFigure = null;
    private double lastMouseX, lastMouseY;
    private Random random = new Random();

    @FXML
    public void initialize() {
        // Привязываем размер Canvas к размеру Pane
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        // Перерисовываем при изменении размера
        canvas.widthProperty().addListener((obs, oldVal, newVal) -> redrawCanvas());
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> redrawCanvas());

        setupMouseHandlers();
    }

    private void setupMouseHandlers() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleMousePressed(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (event.getButton() == MouseButton.PRIMARY) {
            // Ищем фигуру под курсором (с конца списка для z-order)
            for (int i = figures.size() - 1; i >= 0; i--) {
                Figure figure = figures.get(i);
                if (figure.contains(x, y)) {
                    selectedFigure = figure;
                    lastMouseX = x;
                    lastMouseY = y;

                    // Перемещаем выбранную фигуру в конец списка (на передний план)
                    figures.remove(i);
                    figures.add(selectedFigure);

                    redrawCanvas();
                    return;
                }
            }
            selectedFigure = null;
        } else if (event.getButton() == MouseButton.SECONDARY) {
            // Правая кнопка - меняем цвет фигуры
            for (int i = figures.size() - 1; i >= 0; i--) {
                Figure figure = figures.get(i);
                if (figure.contains(x, y)) {
                    figure.setColor(generateRandomColor());
                    redrawCanvas();
                    return;
                }
            }
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (selectedFigure != null && event.getButton() == MouseButton.PRIMARY) {
            double deltaX = event.getX() - lastMouseX;
            double deltaY = event.getY() - lastMouseY;

            selectedFigure.move(deltaX, deltaY);
            lastMouseX = event.getX();
            lastMouseY = event.getY();

            redrawCanvas();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            selectedFigure = null;
        }
    }

    private void redrawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Рисуем сетку для удобства
        drawGrid(gc);

        // Рисуем все фигуры
        for (Figure figure : figures) {
            figure.draw(gc);
        }
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        // Вертикальные линии
        for (double x = 0; x <= width; x += 50) {
            gc.strokeLine(x, 0, x, height);
        }

        // Горизонтальные линии
        for (double y = 0; y <= height; y += 50) {
            gc.strokeLine(0, y, width, y);
        }
    }

    private Color generateRandomColor() {
        return Color.color(
                random.nextDouble(),
                random.nextDouble(),
                random.nextDouble()
        );
    }

    // Методы для кнопок

    @FXML
    private void addRectangle() {
        double x = random.nextDouble() * (canvas.getWidth() - 100) + 50;
        double y = random.nextDouble() * (canvas.getHeight() - 100) + 50;
        double width = random.nextDouble() * 100 + 50;
        double height = random.nextDouble() * 100 + 50;

        figures.add(new Rectangle(x, y, width, height, generateRandomColor()));
        redrawCanvas();
    }

    @FXML
    private void addCircle() {
        double x = random.nextDouble() * (canvas.getWidth() - 100) + 50;
        double y = random.nextDouble() * (canvas.getHeight() - 100) + 50;
        double radius = random.nextDouble() * 50 + 25;

        figures.add(new Circle(x, y, radius, generateRandomColor()));
        redrawCanvas();
    }

    @FXML
    private void addTriangle() {
        double x = random.nextDouble() * (canvas.getWidth() - 100) + 50;
        double y = random.nextDouble() * (canvas.getHeight() - 100) + 50;
        double size = random.nextDouble() * 80 + 40;

        figures.add(new Triangle(x, y, size, generateRandomColor()));
        redrawCanvas();
    }

    @FXML
    private void addSquare() {
        double x = random.nextDouble() * (canvas.getWidth() - 100) + 50;
        double y = random.nextDouble() * (canvas.getHeight() - 100) + 50;
        double side = random.nextDouble() * 80 + 40;

        figures.add(new Square(x, y, side, generateRandomColor()));
        redrawCanvas();
    }

    @FXML
    private void clearCanvas() {
        figures.clear();
        redrawCanvas();
    }

    @FXML
    private void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Справка");
        alert.setHeaderText("Управление приложением");
        alert.setContentText(
                "ЛЕВАЯ КНОПКА МЫШИ:\n" +
                        "- Нажмите на фигуру для выбора\n" +
                        "- Перетащите для перемещения\n\n" +
                        "ПРАВАЯ КНОПКА МЫШИ:\n" +
                        "- Нажмите на фигуру для смены цвета\n\n" +
                        "КНОПКИ:\n" +
                        "- Добавляют соответствующие фигуры\n" +
                        "- Clear - очищает холст"
        );
        alert.showAndWait();
    }
}