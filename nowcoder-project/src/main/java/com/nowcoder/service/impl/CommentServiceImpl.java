package com.nowcoder.service.impl;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
public class CommentServiceImpl implements CommentService{
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentDAO commentDAO;

    /*通过entityId和entityType查询评论列表，有可能是对news的评论，有可能是对comment的评论*/
    @Override
    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.selectByEntity(entityId,entityType);
    }

    @Override
    /*添加评论采用了线程池,因为热点问题，同样的情况有 秒杀下单 */
    /*将Service层的服务异步化，在addComment方法上增加注解@Async("asyncServiceExecutor")，
    asyncServiceExecutor方法是前面ExecutorConfig.java中的方法名，
    表明executeAsync方法进入的线程池是asyncServiceExecutor方法创建的*/
    public int addComment(Comment comment) {
        return commentDAO.addComment(comment);
    }

    @Override
    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId,entityType);
    }
}
