package breach;

public interface BreachEvent {
    void onBreachRunning(BreachTask breach);
    void onBreachSuccess(BreachTask breach);
    void onBreachFailed(BreachTask breach);
}
