package yzl.swu.yyreader.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;


public class BookComment {

    private Long id;


    private Long bookId;


    private String commentContent;


    private Integer replyCount;


    private Byte auditStatus;


    private Long createUserId;

    @JsonSerialize(using = CommentUserNameSerialize.class)
    private String createUserName;

    private String createUserPhoto;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Override
    public String toString() {
        return super.toString();
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getBookId() {
        return bookId;
    }


    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }


    public String getCommentContent() {
        return commentContent;
    }


    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent == null ? null : commentContent.trim();
    }


    public Integer getReplyCount() {
        return replyCount;
    }


    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }


    public Byte getAuditStatus() {
        return auditStatus;
    }


    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Long getCreateUserId() {
        return createUserId;
    }


    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserPhoto() {
        return createUserPhoto;
    }

    public void setCreateUserPhoto(String createUserPhoto) {
        this.createUserPhoto = createUserPhoto;
    }

    class CommentUserNameSerialize extends JsonSerializer<String> {

        @Override
        public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

            if(StringUtils.isNotBlank(s)){
                jsonGenerator.writeString(s.substring(0, 4) + "****" + s.substring(s.length() - 3));
            }

        }
    }
}
