package calculator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CalculatorController {
    @FXML
    private TextField display;

    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    @FXML
    public void initialize() {
        display.setText("0");
        setupKeyboardSupport();
    }

    private void setupKeyboardSupport() {
        display.setOnKeyPressed(this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case DIGIT0: case DIGIT1: case DIGIT2: case DIGIT3: case DIGIT4:
            case DIGIT5: case DIGIT6: case DIGIT7: case DIGIT8: case DIGIT9:
                handleNumber(event.getText());
                break;
            case DECIMAL: case PERIOD:
                handleDecimal();
                break;
            case ADD:
                handleOperator("+");
                break;
            case SUBTRACT:
                handleOperator("-");
                break;
            case MULTIPLY:
                handleOperator("×");
                break;
            case DIVIDE:
                handleOperator("/");
                break;
            case ENTER: case EQUALS:
                calculate();
                break;
            case ESCAPE: case DELETE:
                clear();
                break;
            case BACK_SPACE:
                handleBackspace();
                break;
        }
        event.consume();
    }

    private void handleBackspace() {
        String currentText = display.getText();
        if (currentText.length() > 1) {
            display.setText(currentText.substring(0, currentText.length() - 1));
        } else {
            display.setText("0");
            startNewNumber = true;
        }
    }

    @FXML
    private void handleNumber(javafx.event.ActionEvent event) {
        String number = ((javafx.scene.control.Button) event.getSource()).getText();
        handleNumber(number);
    }

    private void handleNumber(String number) {
        if (startNewNumber) {
            display.setText(number);
            startNewNumber = false;
        } else {
            display.setText(display.getText() + number);
        }
    }

    @FXML
    private void handleOperator(javafx.event.ActionEvent event) {
        String op = ((javafx.scene.control.Button) event.getSource()).getText();
        handleOperator(op);
    }

    private void handleOperator(String op) {
        if (!operator.isEmpty()) {
            calculate();
        }

        try {
            firstNumber = Double.parseDouble(display.getText());
            operator = op;
            startNewNumber = true;
        } catch (NumberFormatException e) {
            showError("Некорректное число");
        }
    }

    @FXML
    private void calculate() {
        if (operator.isEmpty()) return;

        try {
            double secondNumber = Double.parseDouble(display.getText());
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "×":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    if (secondNumber == 0) {
                        showError("Деление на ноль невозможно!");
                        return;
                    }
                    result = firstNumber / secondNumber;
                    break;
            }

            // Форматируем результат
            if (result == (long) result) {
                display.setText(String.valueOf((long) result));
            } else {
                display.setText(String.valueOf(result));
            }

            operator = "";
            startNewNumber = true;

        } catch (NumberFormatException e) {
            showError("Некорректное число");
        }
    }

    @FXML
    private void clear() {
        display.setText("0");
        firstNumber = 0;
        operator = "";
        startNewNumber = true;
    }

    @FXML
    private void toggleSign() {
        try {
            double value = Double.parseDouble(display.getText());
            value = -value;
            if (value == (long) value) {
                display.setText(String.valueOf((long) value));
            } else {
                display.setText(String.valueOf(value));
            }
        } catch (NumberFormatException e) {
            showError("Некорректное число");
        }
    }

    @FXML
    private void percent() {
        try {
            double value = Double.parseDouble(display.getText());
            value = value / 100;
            display.setText(String.valueOf(value));
            startNewNumber = true;
        } catch (NumberFormatException e) {
            showError("Некорректное число");
        }
    }

    @FXML
    private void handleDecimal() {
        if (startNewNumber) {
            display.setText("0.");
            startNewNumber = false;
        } else if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        clear();
    }
}