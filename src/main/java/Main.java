import breach.BreachEvent;
import breach.BreachTask;
import decryptor.BreachDecryptor;
import settings.SecurityProperties;

public class Main {

    public static void main(String[] args) {
        SecurityProperties props = new SecurityProperties();
        props.setBreachSize(7);
        props.setMinPuffer(3);
        props.setMaxPuffer(8);
        setSecurityProperties(props);

        //DriverChecker.driverUpdates();

        BreachTask breach = new BreachTask(50);
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
        breach.run();
        BreachDecryptor.decrypt(breach);
    }

    public static void setSecurityProperties(SecurityProperties properties) {
        properties.checkProperties();
    }
}
