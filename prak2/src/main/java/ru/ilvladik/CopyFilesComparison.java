package ru.ilvladik;

import org.apache.commons.io.FileUtils;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;

public class CopyFilesComparison {
    public static void main(String[] args) {
        System.out.println("Ильин Владислав Виктрович ИКБО-01-21");
        final String source = "src/main/resources/source.txt";
        final String dest = "src/main/resources/dest.txt";
        createFile(source, 30);

        calculateTimeAndMemory("FileStreams", () -> copyUsingFileStreams(source, dest));
        calculateTimeAndMemory("FileChannel", () -> copyUsingFileChannel(source, dest));
        calculateTimeAndMemory("ApacheCommonsIO", () -> copyUsingApacheCommonsIO(source, dest));
        calculateTimeAndMemory("FilesClass", () -> copyUsingFilesClass(source, dest));
    }

    private static void createFile(String fileName, int sizeInMBs) {
        byte[] data = new byte[1024 * 1024];
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            for (int i = 0; i < sizeInMBs; i++) {
                out.write(data);
            }
        } catch (IOException e ) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Ильин Владислав Виктрович ИКБО-01-21
    private static Void copyUsingFileStreams(String source, String dest) {
        try (FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    private static Void copyUsingFileChannel(String source, String dest) {
        try (FileInputStream in = new FileInputStream(source);
             FileOutputStream out = new FileOutputStream(dest)) {
            FileChannel inputChannel = in.getChannel();
            FileChannel outputChannel = out.getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }


    // Ильин Владислав Виктрович ИКБО-01-21
    private static Void copyUsingApacheCommonsIO(String source, String dest) {
        File in = new File(source);
        File out = new File(dest);
        try {
            FileUtils.copyFile(in, out);
        } catch (IOException exception) {}
        return null;
    }

    private static Void copyUsingFilesClass(String source, String dest) {
        Path sourcePath = Path.of(source);
        Path destinationPath = Path.of(dest);
        try {
            Files.copy(sourcePath, destinationPath,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {}
        return null;
    }

    private static void calculateTimeAndMemory(String name, Supplier<?> supplier) {
        long start = System.currentTimeMillis();
        supplier.get();
        long end = System.currentTimeMillis();
        System.out.println(name +  ": " + (end - start));
    }
}
