package yzl.swu.yyreader.models;

import org.litepal.crud.LitePalSupport;

public class BookRecordModel extends LitePalSupport {
    private String book_id;
    private int chapterPos;
    private int pagePos;

    private int text_color;
    private int page_color;
    private int text_size;
    private int anim_type;

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
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

    public int getText_color() {
        return text_color;
    }

    public void setText_color(int text_color) {
        this.text_color = text_color;
    }

    public int getPage_color() {
        return page_color;
    }

    public void setPage_color(int page_color) {
        this.page_color = page_color;
    }

    public int getText_size() {
        return text_size;
    }

    public void setText_size(int text_size) {
        this.text_size = text_size;
    }

    public int getAnim_type() {
        return anim_type;
    }

    public void setAnim_type(int anim_type) {
        this.anim_type = anim_type;
    }
}
