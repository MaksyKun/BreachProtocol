---
id: breach-event
title: Using BreachEvents
---
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