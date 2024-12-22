package processes;

import models.File;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

// Ильин Владислав Викторович ИКБО-01-21
public class FileGenerator implements Runnable {

    private final BlockingQueue<File> queue;
    public FileGenerator(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        String[] fileTypes = new String[] {"XML", "JSON", "XLS"};
        Random random = new Random();
        while(true) {
            try {
                int time = random.nextInt(100, 1000);
                Thread.sleep(time);

                String randomType = fileTypes[random.nextInt(fileTypes.length)];
                int randomSize = random.nextInt(10, 100);
                queue.put(new File(randomType, randomSize));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
