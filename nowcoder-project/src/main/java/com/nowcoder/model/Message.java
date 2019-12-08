package com.nowcoder.model;

import java.util.Date;

/**
 * 站内信message，私发
 */
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;//内容
    private Date createdDate;
    private int hasRead;//0是没读，1是读了
    private String conversationId;//conversationId是由对话的双方id组成

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    /*始终保持前小后大，个人感觉应该没有前小后大这个限制*/
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
