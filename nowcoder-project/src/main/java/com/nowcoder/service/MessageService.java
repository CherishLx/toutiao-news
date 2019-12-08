package com.nowcoder.service;

import com.nowcoder.model.Message;
import java.util.List;

/**
 * messageService层
 */
public interface MessageService {

    int addMessage(Message message);

    int deleteMessage(String conversationId);

    int deleteMessageById(int id);

    List<Message> getConversationList(int userId, int offset, int limit);

    List<Message> getConversationDetail(String conversationId, int offset, int limit);

    int getUnreadCount(int userId, String conversationId);

    int updateMessageHasRead(int hasRead, String conversationId);

    Message getMessageById(int id);
}