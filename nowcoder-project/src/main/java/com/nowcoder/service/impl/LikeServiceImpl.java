package com.nowcoder.service.impl;

import com.nowcoder.service.LikeService;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class LikeServiceImpl implements LikeService{
    @Autowired
    JedisAdapter jedisAdapter;

    /*获取某个user对某个类型entityType的某个entityId点赞，点踩的情况，1点赞，-1点踩，0没有点赞点踩*/
    @Override
    public int getLikeStatus(int userId, int entityType, int entityId) {
       String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
       if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
           return 1;
       }
       String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
       return jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1 : 0;
    }

    /*实现当前用户点赞功能，最后返回总共赞的数量*/
    @Override
    public long like(int userId, int entityType, int entityId) {
       //在喜欢集合里增加
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        //从反对里删除
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        /*返回集合中元素的数量*/
        return jedisAdapter.scard(likeKey);
    }

    /*实现当前用户点踩功能，最后返回总共点踩的数量*/
    @Override
    public long disLike(int userId, int entityType, int entityId) {
        //在反对集合里增加
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));

        //从喜欢里删除
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}
