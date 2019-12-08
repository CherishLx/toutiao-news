package com.nowcoder.service;

/**
 * 点赞service层
 */
public interface LikeService {

    int getLikeStatus(int userId, int entityType, int entityId);

    long like(int userId, int entityType, int entityId);

    long disLike(int userId, int entityType, int entityId);
}