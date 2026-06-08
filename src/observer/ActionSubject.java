package observer;

import java.util.ArrayList;
import java.util.List;

public class ActionSubject {

    private final List<ActionObserver> observers = new ArrayList<>();

    public void addObserver(ActionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ActionObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String action) {
        for (ActionObserver observer : observers) {
            observer.onAction(action);
        }
    }
}
