package com.nowcoder.async;

import java.util.List;

/**
 * 统一消费者接口
 */
public interface EventHandler {
    /*能够处理的事件类型*/
    List<EventType> getSupportEventTypes();

    /*如何处理事件*/
    void doHandle(EventModel model);
}
