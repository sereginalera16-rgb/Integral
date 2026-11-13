package ru.gr05504.application;

import ru.gr05504.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class CalculationService {
    private final CalculationRepository repository;
    private final FormulaParser parser;
    // Субъект (Subject) паттерна Наблюдатель: список подписчиков
    private final List<CalculationObserver> observers = new ArrayList<>();

    public CalculationService(CalculationRepository repository, FormulaParser parser) {
        this.repository = repository;
        this.parser = parser;
    }

    public void addObserver(CalculationObserver observer) {
        observers.add(observer);
        observer.calculationsChanged(getAll());   // сразу показываем текущее состояние
    }

    public void removeObserver(CalculationObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        List<Calculation> all = getAll();
        for (CalculationObserver observer : observers) observer.calculationsChanged(all);
    }

    public boolean contains(Calculation calculation) {
        return calculation != null && repository.findAll().contains(calculation);
    }

    // Разбирает формулу, вычисляет интеграл и сохраняет расчёт
    public Calculation calculate(String formula, double lower, double upper,
                                 int intervals, MethodRealised method) {
        DoubleUnaryOperator f = parser.parse(formula);
        double result = Integrator.integrate(f, lower, upper, intervals, method);
        Calculation calculation = new Calculation(formula, lower, upper, intervals, method, result);
        repository.save(calculation);
        notifyObservers();
        return calculation;
    }

    public void delete(Calculation calculation) {
        repository.delete(calculation);
        notifyObservers();
    }

    public void restore(Calculation calculation){
        repository.save(calculation);
        notifyObservers();
    }

    public List<Calculation> getAll() {
        return repository.findAll();
    }
}