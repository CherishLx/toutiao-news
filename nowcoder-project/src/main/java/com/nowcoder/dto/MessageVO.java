package com.nowcoder.dto;

import com.nowcoder.model.Message;
import com.nowcoder.model.User;

/**
 * 站内信+用户+未读数DTO类
 */
public class MessageVO {
    private Message message;
    private User user;
    private int unReadCount;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
