import breach.BreachEntry;
import breach.BreachWindow;
import device.DriverChecker;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    @Getter
    public static final List<File> drivers = new ArrayList<>();
    @Setter
    public static BreachWindow breach = null;

    public static void main(String[] args) {
        DriverChecker.driverUpdates();
    }
}
