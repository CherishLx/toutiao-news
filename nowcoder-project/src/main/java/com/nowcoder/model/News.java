package com.nowcoder.model;

import java.util.Date;

/**
 * 咨询news
 */
public class News {
    private int id;
    private String title;
    private String link;
    private String image;
    private int likeCount;
    private int commentCount;//?news的评论数量
    private Date createdDate;
    private int userId;//发布人ID

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
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

    public String getImage() {
      return image;
    }

    public void setImage(String image) {
      this.image = image;
    }

    public int getLikeCount() {
      return likeCount;
    }

    public void setLikeCount(int likeCount) {
      this.likeCount = likeCount;
    }

    public int getCommentCount() {
      return commentCount;
    }

    public void setCommentCount(int commentCount) {
      this.commentCount = commentCount;
    }

    public Date getCreatedDate() {
      return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
      this.createdDate = createdDate;
    }

    public int getUserId() {
      return userId;
    }

    public void setUserId(int userId) {
      this.userId = userId;
    }
}
