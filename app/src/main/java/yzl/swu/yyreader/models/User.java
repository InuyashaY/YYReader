package yzl.swu.yyreader.models;

import java.util.Date;

public class User {
    private Long id;

    private String username;

    private String password;

    private String nickName;


    private String userPhoto;


    private Byte userSex;


    private Long accountBalance;


    private Byte status;


    private Date createTime;


    private Date updateTime;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }


    public String getNickName() {
        return nickName;
    }


    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }


    public String getUserPhoto() {
        return userPhoto;
    }


    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto == null ? null : userPhoto.trim();
    }


    public Byte getUserSex() {
        return userSex;
    }


    public void setUserSex(Byte userSex) {
        this.userSex = userSex;
    }


    public Long getAccountBalance() {
        return accountBalance;
    }


    public void setAccountBalance(Long accountBalance) {
        this.accountBalance = accountBalance;
    }


    public Byte getStatus() {
        return status;
    }


    public void setStatus(Byte status) {
        this.status = status;
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
