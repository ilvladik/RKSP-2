package ru.ilvladik.task4;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class FileProcessor {
    private final String supportedFileType;


    public FileProcessor(String supportedFileType) {
        this.supportedFileType = supportedFileType;
    }

    // Ильин Владислав Викторович
    public Completable processFiles(Observable<File> fileObservable) {
        return fileObservable
                .filter(file -> file.getFileType().equals(supportedFileType))
.flatMapCompletable(file -> {
            long processingTime = file.getFileSize() * 7;
            return Completable
                    .fromAction(() -> {
                        Thread.sleep(processingTime);
                        System.out.println("Processed " +
                                supportedFileType + " file with size " + file.getFileSize());
                    })
                    .subscribeOn(Schedulers.io())
.observeOn(Schedulers.io());
        })
                .onErrorComplete();
    }
}
