package device;

import breach.BreachUT;
import settings.SecurityProperties;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUT {
    private final Map<File, String> passwords = new TreeMap<>();

    public static void lock(File driver) {
        File zipFile = new File(driver, "___" + BreachUT.getRandomHexOrder(SecurityProperties.encryptedFileLength) + ".zip");
        zip(driver, zipFile);
    }

    public static void unlock(File driver) {
        deleteEmptyZip(driver);
        for (File file : driver.listFiles()) {
            if (file.getName().startsWith("___") && file.getName().endsWith(".zip") && !file.isHidden()) {
                unzip(file, driver);
                deleteFile(file);
            }
        }
    }

    public static void createEmptyZip(File driver) {
        File file = new File(driver, "___" + BreachUT.getRandomHexOrder(BreachUT.random.nextInt(SecurityProperties.minEncryptLength, SecurityProperties.maxEncryptLength)) + ".zip");
        try {
            if(!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileInputStream input = new FileInputStream(file);
             ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(file));) {
            zip.putNextEntry(new ZipEntry(file.getName()));
            byte[] buffer = new byte[1024];
            int len;
            //copy the file to the zip
            while ((len = input.read(buffer)) > 0) {
                zip.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEmptyZip(File driver) {
        for(File file : driver.listFiles()) {
            String fileName = file.getName();
            if(fileName.startsWith("___") && fileName.endsWith(".zip")) {
                int size = fileName.replace("___", "").replace(".zip", "").split("-").length;
                if(size < SecurityProperties.encryptedFileLength) {
                    deleteFile(file);
                }
            }
        }
    }

    private static void zip(File sourceDir, File zipFile) {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (File file : sourceDir.listFiles()) {
                if (!file.getName().startsWith("___") && !file.isHidden()) {
                    zipFile(file, file.getName(), zos);
                    deleteFile(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteFile(File file) {
        try {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deleteFile(f);
                }
                Files.delete(file.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void zipFile(File file, String fileName, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            // For directories, add an entry and zip its contents
            zos.putNextEntry(new ZipEntry(fileName + "/"));

            File[] files = file.listFiles();
            if (files != null) {
                for (File nestedFile : files) {
                    zipFile(nestedFile, fileName + "/" + nestedFile.getName(), zos);
                }
            }
        } else {
            // For files, add an entry and write the file content
            zos.putNextEntry(new ZipEntry(fileName));

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
            }
        }
    }

    private static void unzip(File zipFile, File outputDir) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                Path entryPath = new File(outputDir, entryName).toPath();

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());

                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(entryPath.toFile()))) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            bos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
            }
        } catch (IOException ignored) {
        }
    }
}
