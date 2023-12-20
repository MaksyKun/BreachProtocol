package breach.logic;

public interface BreachEvent {
    default void onBreachPreRunning(BreachTask breach) {}
    default void onBreachPostRunning(BreachTask breach) {}
    default void onBreachSuccess(BreachTask breach) {}
    default void onBreachFailed(BreachTask breach) {}
}
