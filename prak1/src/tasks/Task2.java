package tasks;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class Task2 {
    // Ильин Владислав Викторович ИКБО-01-21
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("exit"))
                break;
            try {
                double number = Double.parseDouble(line);
                CompletableFuture.supplyAsync(() -> square(number))
                        .thenAccept(System.out::println);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number: " + line);
            }
        }
    }

    private static Double square(double number) {
        try {
            Thread.sleep(new Random().nextInt(1, 5) * 1000L);
        } catch (InterruptedException exception) {}
        return number * number;
    }
}
