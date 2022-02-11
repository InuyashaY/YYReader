package yzl.swu.yyreader.models;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 小说的章节
 * */
public class TxtChapterModel extends LitePalSupport {
    //id
    private String id;
    //章节名
    @SerializedName("indexName")
    public String title;
    //章节链接
    String link;
    //章节的起始和结束位置
    public long start;
    public long end;
    //书籍id
    private long book_id;

    private Integer indexNum;

    private Integer wordCount;

    private Byte isVip;

    private Integer bookPrice;

    private String storageType;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TxtChapterModel(long book_id) {
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

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public Integer getIndexNum() {
        return indexNum;
    }


    public void setIndexNum(Integer indexNum) {
        this.indexNum = indexNum;
    }


    public Integer getWordCount() {
        return wordCount;
    }


    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }


    public Byte getIsVip() {
        return isVip;
    }


    public void setIsVip(Byte isVip) {
        this.isVip = isVip;
    }


    public Integer getBookPrice() {
        return bookPrice;
    }


    public void setBookPrice(Integer bookPrice) {
        this.bookPrice = bookPrice;
    }


    public String getStorageType() {
        return storageType;
    }


    public void setStorageType(String storageType) {
        this.storageType = storageType == null ? null : storageType.trim();
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
