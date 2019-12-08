package com.nowcoder.service;

import com.nowcoder.model.Comment;

import java.util.List;

/**
 * CommentService接口
 */
public interface CommentService {

    public List<Comment> getCommentsByEntity(int entityId, int entityType);

    public int addComment(Comment comment);

    public int getCommentCount(int entityId,int entityType);
}
