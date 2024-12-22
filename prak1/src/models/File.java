package models;


// Ильин Владислав Викторович ИКБО-01-21
public class File {
    private String fileType;
    private int fileSize;

    public File(String fileType, int fileSize) {
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
