package com.nowcoder.service.impl;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageDAO messageDAO;

    @Override
    public int addMessage(Message message) {
        return messageDAO.addMessage(message);
    }

    @Override
    public int deleteMessage(String conversationId){
        return messageDAO.deleteMessage(conversationId);
    }

    @Override
    public int deleteMessageById(int id){
        return messageDAO.deleteMessageById(id);
    }
    /*和我有关的会话列表，from_id或to_id*/
    @Override
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId,offset,limit);
    }

    /*根据conversationId（由from_id和to_id组成）获取会话详情*/
    @Override
    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }

    /*获取当前会话conversationId，发送给我to_id，没有阅读的message数量*/
    @Override
    public int getUnreadCount(int userId,String conversationId) {
        return messageDAO.getConversationUnreadCount(userId,conversationId);
    }

    /*这个方法不是删除，是已读*/
    @Override
    public int updateMessageHasRead(int hasRead, String conversationId) {
        return messageDAO.updateMessageHasRead(hasRead,conversationId);
    }

    @Override
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }
}
