package examples.client;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class FileEncryption {

    private static final String AES_ALGORITHM = "AES";

    public static byte[] generateKey() {
        try {
            // Create a KeyGenerator instance for AES
            KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);

            // Initialize the KeyGenerator with the desired key size (in bits)
            // For example, you can use 128, 192, or 256 bits for AES encryption
            int keySize = 128;
            keyGen.init(keySize);

            // Generate a random secret key
            SecretKey secretKey = keyGen.generateKey();

            // Get the byte array representation of the secret key
            byte[] keyBytes = secretKey.getEncoded();

            // Use the keyBytes for encryption or store it securely
            System.out.println("Generated AES Secret Key: " + new String(keyBytes));
            return keyBytes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void encryptFilesInDirectory(String directoryPath, byte[] secret) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Directory does not exist.");
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    encryptFile(file, secret);
                    file.delete();
                }
            }
        }
    }

    public static void decryptFilesInDirectory(String directoryPath, byte[] secret) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Directory does not exist.");
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    decryptFile(file, secret);
                    file.delete();
                }
            }
        }
    }

    private static void encryptFile(File file, byte[] secret) {
        try (FileInputStream fis = new FileInputStream(file);
             FileOutputStream fos = new FileOutputStream(file.getPath() + ".encrypted")) {

            byte[] inputBytes = new byte[(int) file.length()];
            fis.read(inputBytes);

            Key secretKey = new SecretKeySpec(secret, AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] outputBytes = cipher.doFinal(inputBytes);

            fos.write(outputBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void decryptFile(File file, byte[] secret) {
        try (FileInputStream fis = new FileInputStream(file);
             FileOutputStream fos = new FileOutputStream(file.getPath().replace(".encrypted", ""))) {

            byte[] inputBytes = new byte[(int) file.length()];
            fis.read(inputBytes);

            Key secretKey = new SecretKeySpec(secret, AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] outputBytes = cipher.doFinal(inputBytes);

            fos.write(outputBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
