package yzl.swu.yyreader.models;

import com.google.gson.annotations.SerializedName;

public class ChapterInfoBean {
    private String title;
    @SerializedName("content")
    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
