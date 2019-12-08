package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 站内信DAO层
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /*添加评论*/
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    /*分页显示和我有关from_id或to_id的message会话列表，这里的tt是临时表的别名*/
    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc)" +
            " tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset, @Param("limit") int limit);

    /*根据id分页显示message信息*/
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    /*获取当前会话conversationId，发送给我to_id，没有阅读的message数量*/
    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    /*设置某条消息已读，这里的已读和删除是不同的概念，应该还少了一个删除方法*/
    @Update({"update ",TABLE_NAME," set has_read=#{hasRead} where conversation_id=#{conversationId}"})
    int updateMessageHasRead(@Param("hasRead") int hasRead,
                             @Param("conversationId") String conversationId);

    /*删除某条消息*/
    @Delete({"delete from ", TABLE_NAME," where conversation_id=#{conversationId}"})
    int deleteMessage(@Param("conversationId") String conversationId);

    @Delete({"delete from ", TABLE_NAME," where id=#{id}"})
    int deleteMessageById(@Param("id") int id);

    @Select({"select ",SELECT_FIELDS ," from ",TABLE_NAME," where id=#{id}"})
    Message getMessageById(@Param("id") int id);

}
