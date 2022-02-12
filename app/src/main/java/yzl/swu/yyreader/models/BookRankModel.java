package yzl.swu.yyreader.models;

import java.util.Date;

public class BookRankModel {

    private Long id;


    private Byte workDirection;


    private Integer catId;


    private String catName;


    private String picUrl;


    private String bookName;


    private Long authorId;


    private String authorName;


    private String bookDesc;


    private Float score;


    private Byte bookStatus;


    private Long visitCount;


    private Integer wordCount;


    private Integer commentCount;


    private Integer yesterdayBuy;


    private Long lastIndexId;


    private String lastIndexName;


    private Date lastIndexUpdateTime;


    private Byte isVip;


    private Byte status;


    private Date updateTime;


    private Date createTime;


    private Integer crawlSourceId;


    private String crawlBookId;


    private Date crawlLastTime;


    private Byte crawlIsStop;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Byte getWorkDirection() {
        return workDirection;
    }


    public void setWorkDirection(Byte workDirection) {
        this.workDirection = workDirection;
    }


    public Integer getCatId() {
        return catId;
    }


    public void setCatId(Integer catId) {
        this.catId = catId;
    }


    public String getCatName() {
        return catName;
    }


    public void setCatName(String catName) {
        this.catName = catName == null ? null : catName.trim();
    }


    public String getPicUrl() {
        return picUrl;
    }


    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }


    public String getBookName() {
        return bookName;
    }


    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
    }


    public Long getAuthorId() {
        return authorId;
    }


    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }


    public String getAuthorName() {
        return authorName;
    }


    public void setAuthorName(String authorName) {
        this.authorName = authorName == null ? null : authorName.trim();
    }


    public String getBookDesc() {
        return bookDesc == null || bookDesc.isEmpty() ? "暂无简介" : bookDesc.replace("&nbsp", " ").replace("<br/>", " ").replace(";", " ");
    }


    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc == null ? null : bookDesc.trim();
    }


    public Float getScore() {
        return score;
    }


    public void setScore(Float score) {
        this.score = score;
    }


    public Byte getBookStatus() {
        return bookStatus;
    }


    public void setBookStatus(Byte bookStatus) {
        this.bookStatus = bookStatus;
    }


    public Long getVisitCount() {
        return visitCount;
    }


    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }


    public Integer getWordCount() {
        return wordCount;
    }


    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }


    public Integer getCommentCount() {
        return commentCount;
    }


    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }


    public Integer getYesterdayBuy() {
        return yesterdayBuy;
    }


    public void setYesterdayBuy(Integer yesterdayBuy) {
        this.yesterdayBuy = yesterdayBuy;
    }


    public Long getLastIndexId() {
        return lastIndexId;
    }


    public void setLastIndexId(Long lastIndexId) {
        this.lastIndexId = lastIndexId;
    }


    public String getLastIndexName() {
        return lastIndexName;
    }


    public void setLastIndexName(String lastIndexName) {
        this.lastIndexName = lastIndexName == null ? null : lastIndexName.trim();
    }


    public Date getLastIndexUpdateTime() {
        return lastIndexUpdateTime;
    }


    public void setLastIndexUpdateTime(Date lastIndexUpdateTime) {
        this.lastIndexUpdateTime = lastIndexUpdateTime;
    }


    public Byte getIsVip() {
        return isVip;
    }


    public void setIsVip(Byte isVip) {
        this.isVip = isVip;
    }


    public Byte getStatus() {
        return status;
    }


    public void setStatus(Byte status) {
        this.status = status;
    }


    public Date getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Integer getCrawlSourceId() {
        return crawlSourceId;
    }


    public void setCrawlSourceId(Integer crawlSourceId) {
        this.crawlSourceId = crawlSourceId;
    }


    public String getCrawlBookId() {
        return crawlBookId;
    }


    public void setCrawlBookId(String crawlBookId) {
        this.crawlBookId = crawlBookId == null ? null : crawlBookId.trim();
    }


    public Date getCrawlLastTime() {
        return crawlLastTime;
    }


    public void setCrawlLastTime(Date crawlLastTime) {
        this.crawlLastTime = crawlLastTime;
    }


    public Byte getCrawlIsStop() {
        return crawlIsStop;
    }


    public void setCrawlIsStop(Byte crawlIsStop) {
        this.crawlIsStop = crawlIsStop;
    }
}
