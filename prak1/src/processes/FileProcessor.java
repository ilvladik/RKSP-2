package processes;

import models.File;

import java.util.concurrent.BlockingQueue;

// Ильин Владислав Викторович ИКБО-01-21
public class FileProcessor implements Runnable {
    private final BlockingQueue<File> queue;
    private final String fileType;

    public FileProcessor(BlockingQueue<File> queue, String fileType) {
        this.queue = queue;
        this.fileType = fileType;
    }

    @Override
    public void run() {
        while(true) {
            try {
                File file = queue.take();
                if (file.getFileType() == fileType) {
                    int time = 7 * file.getFileSize();
                    Thread.sleep(time);
                    System.out.println("Processed file with type: " + file.getFileType());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
