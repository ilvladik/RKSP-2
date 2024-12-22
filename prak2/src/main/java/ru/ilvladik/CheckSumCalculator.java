package ru.ilvladik;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CheckSumCalculator {
    public static void main(String[] args) {
        System.out.println("Ильин Владислав Викторович ИКБО-01-21");
        final String filePath = "src/main/resources/data.txt";
        try {
            short checksum = calculateCheckSum(filePath);
            System.out.printf("Контрольная сумма файла %s: 0x%04X%n", filePath,
                    checksum);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static short calculateCheckSum(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             FileChannel fileChannel = fileInputStream.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            short checkSum = 0;
            while (fileChannel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    checkSum ^= buffer.get();
                }
                buffer.clear();
            }
            return checkSum;
        }
    }
}
