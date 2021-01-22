package yzl.swu.yyreader.models;

public class LocalFileModel {
    private String fileTitle;
    private String fileSize;
    private String fileDate;

    public LocalFileModel(String fileTitle, String fileSize, String fileDate) {
        this.fileTitle = fileTitle;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }
}
