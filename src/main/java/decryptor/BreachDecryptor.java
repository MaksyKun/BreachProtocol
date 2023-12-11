package decryptor;

import breach.BreachWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BreachDecryptor {

    public static void decrypt(BreachWindow breach) {
        try {
            Thread.sleep(1000);
            while (breach.solveAmount < breach.level && breach.state == BreachWindow.BreachState.RUNNING) {
                for (Integer[] entry : breach.master) {
                    breach.updateCurrentClick(breach.breachEntries[entry[0]][entry[1]]);
                    Thread.sleep(25);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static BreachFile getEncryptionFile(File driver) {
        for(File file : driver.listFiles()) {
            if(file.getName().contains("Encryption.breach")) {
                try (FileInputStream input = new FileInputStream(file)){
                    String[] data = new String(input.readAllBytes(), StandardCharsets.UTF_8).split("\n");
                    int breachAmount= Integer.parseInt(data[0].split("=")[1]);
                    double time = Double.parseDouble(data[1].split("=")[1]);
                    boolean autoDecrypt = Boolean.parseBoolean(data[0].split("=")[1]);
                    return new BreachFile(breachAmount, time, autoDecrypt);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }
        return null;
    }
}
