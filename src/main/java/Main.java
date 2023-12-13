import breach.*;
import decryptor.BreachDecryptor;
import settings.SecurityProperties;

public class Main {

    public static void main(String[] args) {
        SecurityProperties.checkProperties();
        //DriverChecker.driverUpdates();

        BreachTask breach = new BreachTask(100);
        breach.createEvent(new BreachEvent() {
            @Override
            public void onBreachRunning(BreachTask breach) {
                System.out.println("Test");
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

        breach.setRunnable(() -> {});
        breach.setSuccessCallback(() -> {});
        breach.setFailedCallback(() -> {});
        breach.run();
        BreachDecryptor.decrypt(breach);
    }
}
