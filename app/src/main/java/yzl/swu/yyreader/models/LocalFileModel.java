package yzl.swu.yyreader.models;

import yzl.swu.yyreader.common.FileType;

public class LocalFileModel {
    private String fileTitle;
    private String fileSize;
    private String fileDate;
    private FileType fileType;

    public LocalFileModel(String fileTitle) {
        this.fileTitle = fileTitle;
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

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
