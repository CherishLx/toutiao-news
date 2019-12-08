package com.nowcoder.async;

/**
 * 枚举类enum
 * 事件的类型，点赞，评论，登录，发送邮件
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
