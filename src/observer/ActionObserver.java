package observer;

/**
 * Interface Observer (Design Pattern Observer).
 * Les objets qui souhaitent etre notifies des actions implementent cette interface.
 */
@FunctionalInterface
public interface ActionObserver {

    /** Appelee quand une action est effectuee */
    void onAction(String action);
}
