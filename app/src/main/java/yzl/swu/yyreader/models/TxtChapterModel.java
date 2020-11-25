package yzl.swu.yyreader.models;

public class TxtChapterModel {
    //章节名
    String title;
    //章节链接
    String link;
    //章节的起始和结束位置
    public int start;
    public int end;

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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
