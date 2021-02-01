package yzl.swu.yyreader.models;

import org.litepal.crud.LitePalSupport;

/**
 * 小说的章节
 * */
public class TxtChapterModel extends LitePalSupport {
    //章节名
    public String title;
    //章节链接
    String link;
    //章节的起始和结束位置
    public long start;
    public long end;
    //书籍id
    private int book_id;

    public TxtChapterModel(int book_id) {
        this.book_id = book_id;
    }
    public TxtChapterModel(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
