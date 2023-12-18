# BreachProtocol
BreachProtcol is an API to reconstruct a breach puzzles that are based in Cyberpunk 2077. 
The API allows you to place a breach-window based on JFrame between two transactions 
in Java code in order to have to solve it before the code is executed further. 
This is done using intercepted "BreachEvents", which will be explained again later.

## Initializing the API
It is recommended to initialize the BreachAPI manually before use. 
If this is not done, the default settings are simply used.

## Creating a BreachTask
BreachTask is the mentioned object that is instantiated as a breach puzzle to be used.
The breach function is set up through the three runnables that are initialized below.
```java
public static void main(String args[]) {

  // The parameter inside BreachTask(level) is meant to set the 'depth' of your breach.
  // level=5 means there will be 5 breach-puzzles in a row before it is count as solved.
  BreachTask breach = new BreachTask(5);

  // Native runnables that should be executed when the breach reaches an BreachState (RUNNING, SUCCESS, FAILED)
  breach.setRunnable(() -> {
      // Code that is being executed while the breach is active / not solved
  });
  breach.setFailedCallback(() -> {
      // Code that is being executed when the breach is successfully solved
  });
  breach.setRunnable(() -> {
      // Code that is being executed when the breach was failed to solve
  });

  breach.run();
}
```

## Hooking into existing BreachTask
In case you need to hook into your breach to change stuff, you can create a BreachEvent Interface
that brings 4 hookup events.
```java
breach.createEvent(new BreachEvent() {
  @Override
  public void onBreachPreRunning(BreachTask breach) {
      // Runs before the "running runnable" is being executed
      System.out.println("Test Pre");
  }

  @Override
  public void onBreachPostRunning(BreachTask breach) {
      // Runs after the "running runnable" is being executed
      System.out.println("Test Post");
  }

  @Override
  public void onBreachSuccess(BreachTask breach) {
      // Runs parallel to the success-runnable
      System.out.println("Test Success");
  }

  @Override
  public void onBreachFailed(BreachTask breach) {
      // Runs parallel to the failed-runnable
      System.out.println("Test Failed");
  }
});
```

