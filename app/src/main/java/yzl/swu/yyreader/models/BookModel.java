package yzl.swu.yyreader.models;

public class BookModel extends Object {
    private String bookTitle;
    private int coverResource;
    private String record;

    public BookModel(String bookTitle,int coverResource,String record){
        this.bookTitle = bookTitle;
        this.coverResource = coverResource;
        this.record = record;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getCoverResource() {
        return coverResource;
    }

    public void setCoverResource(int coverResource) {
        this.coverResource = coverResource;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
