package examples.client;

import breach.logic.BreachDecryptor;
import breach.logic.BreachEvent;
import breach.logic.BreachTask;
import net.samuelcampos.usbdrivedetector.USBDeviceDetectorManager;
import net.samuelcampos.usbdrivedetector.events.DeviceEventType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class USBWatcher {

    public static List<String> registeredDevices = new ArrayList<>();
    public static USBDeviceDetectorManager DriveDetector = new USBDeviceDetectorManager();
    public static void watch(boolean minorMode) {
        System.out.println("Watcher for USBDevices enabled...");
        DriveDetector.addDriveListener(entry -> {
            if (registeredDevices.contains(entry.getStorageDevice().getUuid())) {
                System.out.println("Device is registered: " + entry.getStorageDevice().getUuid());
                System.out.println("Accepting Interactions");
            } else {
                System.out.println("Device is not registered: " + entry.getStorageDevice().getUuid());
                System.out.println("Denying Interactions");
                byte[] secretKey = FileEncryption.generateKey();
                if (entry.getEventType() != DeviceEventType.CONNECTED) return;
                FileEncryption.encryptFilesInDirectory(entry.getStorageDevice().getRootDirectory().getPath(), secretKey);
                System.out.println("Driver was encrypted. Generating Icewall...");
                Daemon_Client.ice.setupICE(entry.getStorageDevice().getUuid(), minorMode, entry, secretKey);
                //BreachDecryptor.decrypt(breach);
            }
        });
    }

    public static void watch_decrypt_sim() {
        USBDeviceDetectorManager driveDetector = new USBDeviceDetectorManager();
        driveDetector.addDriveListener(entry -> {
            byte[] secretKey = FileEncryption.generateKey();
            if (entry.getEventType() != DeviceEventType.CONNECTED) return;
            BreachTask breach = new BreachTask(100);
            breach.createEvent(new BreachEvent() {
                @Override
                public void onBreachSuccess(BreachTask breach) {
                    System.out.println("Id: " + entry.getStorageDevice().getUuid());
                    System.out.println("Drive: " + entry.getStorageDevice().getDeviceName() + " was decrypted!");
                    FileEncryption.decryptFilesInDirectory(entry.getStorageDevice().getRootDirectory().getPath(), secretKey);
                    BreachEvent.super.onBreachSuccess(breach);
                }
            });
            breach.setFailedCallback(() -> {
                try {
                    driveDetector.unmountStorageDevice(entry.getStorageDevice());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            breach.setSuccessCallback(() -> System.out.println(entry));
            FileEncryption.encryptFilesInDirectory(entry.getStorageDevice().getRootDirectory().getPath(), secretKey);
            breach.run();
            BreachDecryptor.decrypt(breach);
        });
    }
}
