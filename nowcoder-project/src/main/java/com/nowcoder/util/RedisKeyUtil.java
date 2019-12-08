package com.nowcoder.util;

/**
 * 实现点赞，点踩相关功能的字符串封装成String
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";//分隔符
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";

    /*？*/
    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

    /*封装点赞行为成String*/
    public static String getLikeKey(int entityId, int entityType) {
        return BIZ_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /*封装点踩行为成String*/
    public static String getDisLikeKey(int entityId, int entityType) {
        return BIZ_DISLIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
