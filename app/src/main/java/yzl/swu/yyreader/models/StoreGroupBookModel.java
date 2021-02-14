package yzl.swu.yyreader.models;

public class StoreGroupBookModel {
    int coverResource;
    String title;
    String author;

    public StoreGroupBookModel(int coverResource, String title, String author) {
        this.coverResource = coverResource;
        this.title = title;
        this.author = author;
    }

    public int getCoverResource() {
        return coverResource;
    }

    public void setCoverResource(int coverResource) {
        this.coverResource = coverResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
