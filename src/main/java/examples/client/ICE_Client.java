package examples.client;

import breach.logic.BreachEvent;
import breach.logic.BreachTask;
import examples.server.Daemon_Server;
import examples.server.Firewall;
import net.samuelcampos.usbdrivedetector.events.USBStorageEvent;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class ICE_Client {

    private static final int SecurityGrade = 3;
    private static final int EnhancedDuration = 3000;
    private final Map<String, BreachTask> breaches = new TreeMap<>();

    public void setupICE(String ip, boolean registered, USBStorageEvent entry, byte[] secretKey) {
        if(breaches.containsKey(ip)) return;
        System.out.println("USBDriver is not allowed by the client");
        if(!registered)
            System.out.println("USBDriver is not registered on the client. Setup of Enhanced-ICE...");
        else
            System.out.println("Setup of Standard-ICE...");

        BreachTask task = new BreachTask(SecurityGrade);
        setEvents(task, ip, registered, entry, secretKey);
        breaches.put(ip, task);
        breaches.get(ip).run();
    }

    public void setEvents(BreachTask task, String ip, boolean registered, USBStorageEvent entry, byte[] secretKey) {
        task.createEvent(new BreachEvent() {

            @Override
            public void onBreachPostRunning(BreachTask breach) {
                if (registered) return;
               Thread thread = new Thread(() -> {
                   try {
                       Thread.sleep(EnhancedDuration);
                       runEnhancedBreach(ip, entry, secretKey);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               });
               thread.start();
            }

            @Override
            public void onBreachFailed(BreachTask breach) {
                try {
                    USBWatcher.DriveDetector.unmountStorageDevice(entry.getStorageDevice());
                    System.out.println("Drive: " + entry.getStorageDevice().getDeviceName() + " was unmounted!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onBreachSuccess(BreachTask breach) {
                System.out.println("Id: " + entry.getStorageDevice().getUuid());
                System.out.println("Drive: " + entry.getStorageDevice().getDeviceName() + " was decrypted!");
                FileEncryption.decryptFilesInDirectory(entry.getStorageDevice().getRootDirectory().getPath(), secretKey);
                BreachEvent.super.onBreachSuccess(breach);
            }
        });
    }

    public void runEnhancedBreach(String ip, USBStorageEvent entry, byte[] secretKey) {
        BreachTask task = new BreachTask(SecurityGrade);
        breaches.get(ip).setState(BreachTask.BreachState.FAILED);
        setEvents(task, ip, false, entry, secretKey);
        breaches.put(ip, task);
        breaches.get(ip).run();
    }

    public boolean isBreachFinished(String ip) {
        return breaches.containsKey(ip) && breaches.get(ip).state() != BreachTask.BreachState.RUNNING;
    }

    public void removeBreach(String ip) {
        breaches.remove(ip);
    }
}
