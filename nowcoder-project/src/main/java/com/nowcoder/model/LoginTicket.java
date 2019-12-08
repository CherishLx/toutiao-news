package com.nowcoder.model;

import java.util.Date;

/**
 * LoginTicket类，起token的作用，注册登陆成功的用户会服务器会发送一个ticket，过期后，重新注册登陆成功，再次发送一个ticket
 */
public class LoginTicket {
    private int id;
    private int userId;
    private Date expired;//过期时间
    private int status;// 0有效，1无效
    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
