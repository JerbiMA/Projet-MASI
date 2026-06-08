package observer;

@FunctionalInterface
public interface ActionObserver {

    void onAction(String action);
}
