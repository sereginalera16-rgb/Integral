// ui/CalculationFormPresenter.java
package ru.gr05504.ui;

import ru.gr05504.application.CalculateCommand;
import ru.gr05504.application.CalculationService;
import ru.gr05504.application.CommandHistory;

public class CalculationFormPresenter {
    private final CalculatorView view;
    private final CalculationService service;
    private final CommandHistory history;

    public CalculationFormPresenter(
            CalculatorView view,
            CalculationService service,
            CommandHistory history
    ) {
        this.view = view;
        this.service = service;
        this.history = history;
        // Наблюдаем за историей команд: от неё зависит кнопка «Отменить»
        history.addObserver(this::updateButtons);
    }

    public void calculateClicked() {
        try {
            if (history.execute(buildCommand())) view.clearForm();
        } catch (IllegalArgumentException ex) {
            view.showError(ex.getMessage());
        }
    }

    public void undoLast() {
        history.undo();
        // Список обновлять вручную больше не нужно — это делает Наблюдатель
    }

    // Вызывается окном при любом изменении полей формы
    public void inputChanged() {
        updateButtons();
    }

    public void updateButtons() {
        view.setCalculateEnabled(canCalculate());
        view.setUndoEnabled(history.canUndo());
    }

    private boolean canCalculate() {
        try {
            return buildCommand().canExecute();
        } catch (IllegalArgumentException ex) {
            return false;   // поля пока заполнены неверно
        }
    }

    // Собирает команду из полей формы; при ошибке ввода бросает IllegalArgumentException
    private CalculateCommand buildCommand() {
        String formula = view.getFormula().trim();
        if (formula.isEmpty())
            throw new IllegalArgumentException("Введите формулу, например sin(x)");
        double lower = parseNumber(view.getLowerText(), "Нижний предел");
        double upper = parseNumber(view.getUpperText(), "Верхний предел");
        int intervals = parseInteger(view.getIntervalsText(), "Число отрезков");
        return new CalculateCommand(service, formula, lower, upper, intervals, view.getMethod());
    }

    private double parseNumber(String text, String fieldName) {
        if (text == null || text.isBlank())
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        try {
            // Запятая допускается как десятичный разделитель: 0,5 и 0.5 одинаковы
            return Double.parseDouble(text.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + ": введите число");
        }
    }

    private int parseInteger(String text, String fieldName) {
        if (text == null || text.isBlank())
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + ": введите целое число");
        }
    }
}
