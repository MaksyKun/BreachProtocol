import breach.logic.BreachEvent;
import breach.logic.BreachTask;
import breach.logic.BreachDecryptor;
import settings.SecurityProperties;

public class Main {

    public static void main(String[] args) {
        SecurityProperties props = new SecurityProperties();
        props.setBreachSize(7);
        props.setMinPuffer(3);
        props.setMaxPuffer(8);
        setSecurityProperties(props);

        //DriverChecker.driverUpdates();

        BreachTask breach = new BreachTask(100);
        breach.createEvent(new BreachEvent() {
            @Override
            public void onBreachPreRunning(BreachTask breach) {
                System.out.println("Test Pre");
            }

            @Override
            public void onBreachPostRunning(BreachTask breach) {
                System.out.println("Test Post");
            }

            @Override
            public void onBreachSuccess(BreachTask breach) {
                System.out.println("Test Success");
            }

            @Override
            public void onBreachFailed(BreachTask breach) {
                System.out.println("Test Failed");
            }
        });

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
        BreachDecryptor.decrypt(breach);
    }

    public static void setSecurityProperties(SecurityProperties properties) {
        properties.checkProperties();
    }
}
