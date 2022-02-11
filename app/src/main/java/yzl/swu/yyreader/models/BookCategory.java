package yzl.swu.yyreader.models;

import java.util.Date;

public class BookCategory {

    private Integer id;


    private Byte workDirection;


    private String name;


    private Byte sort;


    private Long createUserId;


    private Date createTime;


    private Long updateUserId;


    private Date updateTime;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public Byte getWorkDirection() {
        return workDirection;
    }


    public void setWorkDirection(Byte workDirection) {
        this.workDirection = workDirection;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }


    public Byte getSort() {
        return sort;
    }


    public void setSort(Byte sort) {
        this.sort = sort;
    }


    public Long getCreateUserId() {
        return createUserId;
    }


    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Long getUpdateUserId() {
        return updateUserId;
    }


    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }


    public Date getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
