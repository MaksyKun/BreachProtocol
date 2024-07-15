package examples.client;

public class Daemon_Client {

    public static final ICE_Client ice = new ICE_Client();

    public static void main(String[] args) {
        USBWatcher.registeredDevices.add("34CFA97B");
        USBWatcher.watch(true);
    }
}
