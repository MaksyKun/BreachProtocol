package device;

import breach.BreachWindow;
import decryptor.BreachDecryptor;
import decryptor.BreachFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class DriverChecker {
    private static final List<String> blacklist = List.of("C:\\", "D:\\", "E:\\");
    private static final Map<File, Boolean> drivers = new TreeMap<>();
    private static final Map<File, BreachWindow> breaches = new TreeMap<>();
    private static final Map<File, BreachFile> encryptions = new TreeMap<>();

    public static void driverUpdates() {
        Logger.getGlobal().info("Initializing driver update thread...");
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    for (File root : File.listRoots()) {
                        if (blacklist.contains(root.toString()) && !drivers.containsKey(root)) {
                            drivers.put(root, true);
                        } else if (!drivers.containsKey(root) && !breaches.containsKey(root)) {
                            BreachFile encryption = BreachDecryptor.getEncryptionFile(root);
                            if(encryption != null) {
                                drivers.put(root, false);
                                System.out.println("The Driver " + root + " was inserted!");
                                breaches.put(root, new BreachWindow(root, encryption.getBreachAmount()));
                                encryptions.put(root, encryption);
                            }
                        }
                    }
                    Thread.sleep(100);

                    for (BreachWindow breach : breaches.values())
                        switch (breach.state) {
                            case RUNNING -> {
                                if (!breach.isVisible()) {
                                    breach.setVisible(true);
                                    if(encryptions.containsKey(breach.driver)) {
                                        BreachDecryptor.decrypt(breach);
                                    }
                                }
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
