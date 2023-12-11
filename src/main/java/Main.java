import breach.BreachUT;
import decryptor.BreachFile;
import device.DriverChecker;
import settings.SecurityProperties;

import java.net.URL;

public class Main {

    public static void main(String[] args) {
        SecurityProperties.checkProperties();
        new BreachFile(BreachUT.random.nextInt(50, 250), 30, true).generate();
        DriverChecker.driverUpdates();
    }

    public static URL getIcon() {
        return Main.class.getResource("resources/icon.png");
    }
}
