package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 生产者producer
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    /*生产了一个事件*/
    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();//其实也就是"EVENT"
            /*将事件加入到redis的队列中，这里应该可以用消息队列？*/
            jedisAdapter.lpush(key, json);
            logger.info("产生了一个异步事件："+eventModel.getType());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
