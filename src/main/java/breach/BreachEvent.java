package breach;

public interface BreachEvent {
    void onBreachPreRunning(BreachTask breach);
    void onBreachPostRunning(BreachTask breach);
    void onBreachSuccess(BreachTask breach);
    void onBreachFailed(BreachTask breach);
}
