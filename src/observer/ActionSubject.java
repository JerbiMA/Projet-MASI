package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Design Pattern Observer - Sujet.
 * Maintient une liste d'observateurs et les notifie quand une action est effectuee.
 */
public class ActionSubject {

    private final List<ActionObserver> observers = new ArrayList<>();

    public void addObserver(ActionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ActionObserver observer) {
        observers.remove(observer);
    }

    /** Notifier tous les observateurs d'une action */
    public void notifyObservers(String action) {
        for (ActionObserver observer : observers) {
            observer.onAction(action);
        }
    }
}
