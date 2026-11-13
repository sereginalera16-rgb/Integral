// ui/CalculationListPresenter.java
package ru.gr05504.ui;

import ru.gr05504.application.CalculationObserver;
import ru.gr05504.application.CalculationService;
import ru.gr05504.application.CommandHistory;
import ru.gr05504.application.DeleteCommand;
import ru.gr05504.domain.Calculation;

import java.util.List;

// Наблюдатель: сервис сам сообщает ему об изменении списка расчётов
public class CalculationListPresenter implements CalculationObserver {
    private final CalculatorView view;
    private final CalculationService service;
    private final CommandHistory history;

    public CalculationListPresenter(
            CalculatorView view,
            CalculationService service,
            CommandHistory history
    ) {
        this.view = view;
        this.service = service;
        this.history = history;
    }

    public void start() {
        service.addObserver(this);   // подписываемся на изменения
    }

    @Override
    public void calculationsChanged(List<Calculation> calculations) {
        view.showCalculations(calculations);
        updateButtons();
    }

    // Вызывается окном при смене выделения в списке
    public void selectionChanged() {
        updateButtons();
    }

    public void updateButtons() {
        view.setDeleteEnabled(deleteCommand().canExecute());
    }

    public void deleteSelected() {
        history.execute(deleteCommand());
    }

    private DeleteCommand deleteCommand() {
        return new DeleteCommand(service, view.getSelected());
    }
}
