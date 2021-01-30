package yzl.swu.yyreader.models;

import org.litepal.crud.LitePalSupport;

public class BookRecordModel extends LitePalSupport {
    private int book_id;
    private int chapterPos;
    private int pagePos;

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getChapterPos() {
        return chapterPos;
    }

    public void setChapterPos(int chapterPos) {
        this.chapterPos = chapterPos;
    }

    public int getPagePos() {
        return pagePos;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }
}
