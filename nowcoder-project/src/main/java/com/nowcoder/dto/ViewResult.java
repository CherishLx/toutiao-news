package com.nowcoder.dto;

import com.nowcoder.model.Comment;
import com.nowcoder.model.News;
import com.nowcoder.model.User;


/**
 * 咨询+用户+赞数DTO类
 */
public class ViewResult {
    private News news;
    private User user;
    private int like;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

}
