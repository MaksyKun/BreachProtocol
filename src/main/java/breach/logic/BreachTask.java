package breach.logic;

import breach.frame.BreachWindow;

import static breach.logic.BreachTask.BreachState.RUNNING;

@SuppressWarnings("unused")
public class BreachTask {

    private final BreachWindow breachWindow;


    // Runnables & Callbacks for BreachState-Events
    private BreachEvent event;
    private Runnable runnable;
    private Runnable successCallback;
    private Runnable failedCallback;

    // Active assets for breaching
    private final int level;
    private int solveAmount = 0;
    private BreachState state = RUNNING;

    public BreachTask(int level) {
        this.level = level;
        breachWindow = new BreachWindow(this);
        breachWindow.continueBreach();
    }

    public BreachEvent event() {
        return event;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Runnable getSuccessCallback() {
        return successCallback;
    }

    public void setSuccessCallback(Runnable successCallback) {
        this.successCallback = successCallback;
    }

    public Runnable getFailedCallback() {
        return failedCallback;
    }

    public void setFailedCallback(Runnable failedCallback) {
        this.failedCallback = failedCallback;
    }

    public int level() {
        return level;
    }

    public int solveAmount() {
        return solveAmount;
    }

    public void incrementBreach() {
        solveAmount++;
    }

    public BreachState state() {
        return state;
    }

    public void setState(BreachState state) {
        this.state = state;
    }

    public void createEvent(BreachEvent event) {
        this.event = event;
    }

    public void updatedBreachPosition(int row, int col) {
        breachWindow.updateCurrentClick(row, col);
    }

    public BreachWindow window() {
        return breachWindow;
    }

    public void run() {
        if (event == null) {
            throw new RuntimeException("BreachEvent cannot be null");
        }
        if (!breachWindow.isVisible())
            breachWindow.setVisible(true);

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    if (Thread.currentThread().isInterrupted())
                        break;

                    switch (state) {
                        case RUNNING -> {
                            event.onBreachPreRunning(this);
                            if (runnable != null)
                                runnable.run();
                            event.onBreachPostRunning(this);
                            if (!Thread.currentThread().isInterrupted())
                                Thread.sleep(1000);
                        }
                        case SUCCESS, FAILED -> {
                            breachWindow.stateLayer().update(this);
                            Thread.currentThread().interrupt();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    state = BreachState.FAILED;
                }
            }
        });
        thread.start();
    }

    public enum BreachState {
        RUNNING,
        FAILED,
        SUCCESS
    }
}
