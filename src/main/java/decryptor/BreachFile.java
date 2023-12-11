package decryptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BreachFile {

    private static final String breachDir = "C:\\Users\\maksy\\IdeaProjects\\BreachProtocol\\src\\main\\resources";
    private final int breachAmount;
    private final double time;
    private final boolean autoDecrypt;

    public BreachFile(int breachAmount, double time, boolean autoDecrypt) {
        this.breachAmount = breachAmount;
        this.time = time;
        this.autoDecrypt = autoDecrypt;
    }

    public void generate() {
        File file = new File(breachDir, "Encryption.breach");
        try(FileOutputStream input = new FileOutputStream(file)) {
            input.write(("BreachAmount=" + breachAmount + "\n").getBytes());
            input.write(("Time=" + time + "\n").getBytes());
            input.write(("AutoDecrypt=" + autoDecrypt + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getBreachAmount() {
        return breachAmount;
    }

    public double getTime() {
        return time;
    }

    public boolean isAutoDecrypt() {
        return autoDecrypt;
    }
}
