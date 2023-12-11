package settings;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SecurityProperties {

    public static int minPuffer = 3;
    public static int maxPuffer = 8;

    public static int minEncryptLength = 3;
    public static int maxEncryptLength = 6;
    public static int encryptedFileLength = 9;


    public static void checkProperties() {
        if(minPuffer < 1 || maxPuffer < 1 || minEncryptLength < 1 || maxEncryptLength < 1) {
            Logger.getGlobal().log(Level.WARNING, "all values have to be at least 1 or greater!");
            throw new RuntimeException();
        }
        if(minPuffer > maxPuffer) {
            Logger.getGlobal().log(Level.WARNING, "minPuffer cannot be higher than maxPuffer!");
            throw new RuntimeException();
        }
        if(minEncryptLength > maxEncryptLength) {
            Logger.getGlobal().log(Level.WARNING, "minEncryptLength cannot be higher than maxEncryptLength!");
            throw new RuntimeException();
        }

        if(maxEncryptLength >= encryptedFileLength) {
            Logger.getGlobal().log(Level.WARNING, "maxEncryptLength has to be at least 1 lesser than encryptedFileLength!");
            throw new RuntimeException();
        }
    }
}
