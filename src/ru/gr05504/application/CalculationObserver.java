package ru.gr05504.application;

import ru.gr05504.domain.Calculation;

import java.util.List;

// Наблюдатель (Observer): получает уведомление, когда список расчётов изменился
public interface CalculationObserver {
    void calculationsChanged(List<Calculation> calculations);
}
