package tasks;

import models.File;
import processes.FileGenerator;
import processes.FileProcessor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Task3 {
    // Ильин Владислав Викторович ИКБО-01-21
    public static void main(String[] args) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(5);
        Thread generatorProcess = new Thread(new FileGenerator(queue));

        Thread xmlProcess = new Thread(new FileProcessor(queue, "XML"));
        Thread jsonProcess = new Thread(new FileProcessor(queue, "JSON"));
        Thread xlsProcess = new Thread(new FileProcessor(queue, "XLS"));

        generatorProcess.start();
        xmlProcess.start();
        jsonProcess.start();
        xlsProcess.start();
    }
}
