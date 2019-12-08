package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消费者，模拟的是消息队列，另外的邮件发送采用线程池实现，因为这里的afterPropertiesSet()方法，无法使用线程池
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    /*时间类型，它的处理列表*/
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    /*这一步主要是注册每个事件的处理列表*/
    public void afterPropertiesSet() {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<>());
                    }

                    // 注册每个事件的处理函数
                    config.get(type).add(entry.getValue());
                }
            }
        }

        // 启动线程去消费事件，这个线程其实只有一个，想改成线程池，但太麻烦了，试过但失败了
        Thread thread = new Thread(() -> {
            // 从队列一直消费
            while (true) {
                String key = RedisKeyUtil.getEventQueueKey();
                List<String> messages = jedisAdapter.brpop(0, key);
                // 第一个元素是队列名字
                for (String message : messages) {
                    if (message.equals(key)) {
                        continue;
                    }

                    EventModel eventModel = JSON.parseObject(message, EventModel.class);
                    // 找到这个事件的处理handler列表
                    if (!config.containsKey(eventModel.getType())) {
                        logger.error("不能识别的事件");
                        continue;
                    }

                    for (EventHandler handler : config.get(eventModel.getType())) {
                        handler.doHandle(eventModel);
                    }
                    logger.info("处理了一个异步事件："+eventModel.getType());
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
