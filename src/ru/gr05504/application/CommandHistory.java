package ru.gr05504.application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CommandHistory {
    private final Deque<Command> history = new ArrayDeque<>();
    // Наблюдатели за историей: им важно знать, появилось ли что отменять
    private final List<Runnable> observers = new ArrayList<>();

    public void addObserver(Runnable observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (Runnable observer : observers) observer.run();
    }

    public boolean execute(Command command){
        if (!command.canExecute()) return false;
        command.execute();
        history.push(command);
        notifyObservers();
        return true;
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public boolean undo(){
        if (history.isEmpty()) return false;
        history.pop().undo();
        notifyObservers();
        return true;
    }
}
