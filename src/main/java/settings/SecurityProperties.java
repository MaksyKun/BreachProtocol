package settings;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SecurityProperties {
    public static int breachSize = 7;
    public static int minPuffer = 3;
    public static int maxPuffer = 8;

    public void checkProperties() {
        if(minPuffer < 1 || maxPuffer < 1) {
            Logger.getGlobal().log(Level.WARNING, "all values have to be at least 1 or greater!");
            throw new RuntimeException();
        }
        if(minPuffer > maxPuffer) {
            Logger.getGlobal().log(Level.WARNING, "minPuffer cannot be higher than maxPuffer!");
            throw new RuntimeException();
        }
    }

    public int getBreachSize() {
        return breachSize;
    }

    public void setBreachSize(int breachSize) {
        SecurityProperties.breachSize = breachSize;
    }

    public int getMinPuffer() {
        return minPuffer;
    }

    public void setMinPuffer(int minPuffer) {
        SecurityProperties.minPuffer = minPuffer;
    }

    public int getMaxPuffer() {
        return maxPuffer;
    }

    public void setMaxPuffer(int maxPuffer) {
        SecurityProperties.maxPuffer = maxPuffer;
    }
}
