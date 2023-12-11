package device;

import breach.BreachWindow;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DriverChecker {

    private static final String MAIN_DRIVE = File.listRoots()[0].toString();
    private static final Map<File, Boolean> drivers = new TreeMap<>();
    private static final Map<File, BreachWindow> breaches = new TreeMap<>();

    public static void driverUpdates() {
        Logger.getGlobal().info("Initializing driver update thread...");
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    for (File root : File.listRoots()) {
                        if (root.toString().equals(MAIN_DRIVE) && !drivers.containsKey(root)) {
                            drivers.put(root, true);
                        } else if (!drivers.containsKey(root) && !breaches.containsKey(root)) {
                            drivers.put(root, false);
                            System.out.println("The Driver " + root + " was inserted!");
                            breaches.put(root, new BreachWindow(root));
                        }
                    }
                    Thread.sleep(100);

                    for (BreachWindow breach : breaches.values())
                        switch (breach.state) {
                            case RUNNING -> {
                                if (!breach.isVisible())
                                    breach.setVisible(true);
                            }
                            case FAILED -> {
                                System.out.println("Access to " + breach.driver + " denied!");
                                drivers.put(breach.driver, false);
                                breaches.remove(breach.driver);
                            }
                            case SUCCESS -> {
                                Thread successThread = new Thread(() -> {
                                    drivers.put(breach.driver, true);
                                    breaches.remove(breach.driver);
                                    FileUT.unlock(breach.driver);
                                    System.out.println("Access to " + breach.driver + " granted!");
                                });
                                successThread.start();
                            }
                        }
                }
            } catch (Exception e) {
                System.getLogger(DriverChecker.class.getName()).log(System.Logger.Level.WARNING, "The driverUpdater seems to be affected by an error:");
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
