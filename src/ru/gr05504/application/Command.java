package ru.gr05504.application;

public interface Command {
    // Можно ли выполнить команду прямо сейчас (по нему включаем/выключаем кнопки)
    boolean canExecute();
    void execute();
    void undo();
}
