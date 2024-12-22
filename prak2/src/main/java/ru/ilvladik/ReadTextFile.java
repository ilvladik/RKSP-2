package ru.ilvladik;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReadTextFile {
    public static void main(String[] args) {
        System.out.println("ИКБО-01-21 Владислав Ильин");
        Path path = Path.of("src/main/resources/data.txt");
        String[] data = {
                "Line 1",
                "Line 2",
                "Line 3"
        };
        writeLinesToFile(path, data);
        readLinesFromFile(path, System.out);
    }

    private static void writeLinesToFile(Path path, String[] lines) {
        try {
            Files.write(path, List.of(lines));
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void readLinesFromFile(Path path, PrintStream stream) {
        try {
            List<String> lines = Files.readAllLines(path);
            stream.println("Содержимое файла: " + path);
            for (String line : lines) {
                stream.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}