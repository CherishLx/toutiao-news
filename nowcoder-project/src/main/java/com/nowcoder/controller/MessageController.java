package com.nowcoder.controller;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.dto.MessageVO;
import com.nowcoder.model.*;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 站内信controller层
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    /*获取站内信列表*/
    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            //List<ViewObject> conversations = new ArrayList<>();
            List<MessageVO> conversations=new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                //ViewObject vo = new ViewObject();
                MessageVO messageVO=new MessageVO();
                /*一个会话conversation*/
                //vo.set("conversation", msg);
                messageVO.setMessage(msg);
                /*另一方user，targetId*/
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                //vo.set("user", user);
                messageVO.setUser(user);
                /*站内信message没有阅读的数量*/
                //vo.set("unread", messageService.getUnreadCount(localUserId, msg.getConversationId()));
                messageVO.setUnReadCount(messageService.getUnreadCount(localUserId, msg.getConversationId()));
                //conversations.add(vo);
                conversations.add(messageVO);
            }
            model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    /*获取具体的某一条站内信详情*/
    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            //List<ViewObject> messages = new ArrayList<>();
            List<MessageVO> messages = new ArrayList<>();
            for (Message msg : conversationList) {
                //ViewObject vo = new ViewObject();这里改成了用MessageVO做传输对象
                MessageVO messageVO = new MessageVO();
                //vo.set("message", msg);
                messageVO.setMessage(msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                //vo.set("headUrl", user.getHeadUrl());
                //vo.set("userId", user.getId());
                messageVO.setUser(user);
                //messages.add(vo);
                messages.add(messageVO);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取详情消息失败" + e.getMessage());
        }
        return "letterDetail";
    }

    /*controller层的添加message，应该还有一个删除message*/
    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.GET})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            //msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            //手动改了conversationId
            msg.setConversationId(fromId+"_"+toId);
            messageService.addMessage(msg);
            return ToutiaoUtil.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("添加站内信失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "添加站内信失败");
        }
    }

    @RequestMapping(path = {"/msg/deleteMessage"}, method = {RequestMethod.GET})
    public String deleteMessage(@RequestParam("conversationId") String conversationId){
        try{
            messageService.deleteMessage(conversationId);
        }catch (Exception e){
            logger.error("删除站内信失败"+e.getMessage());
        }
        /*return "/msg/list";这个是错的，必须是重定向，重新再一次请求*/
        return "redirect:/msg/list";
    }

    @RequestMapping(path = {"/msg/deleteMessageById"}, method = {RequestMethod.GET})
    public String deleteMessageById(@RequestParam("id") int id){
        Message message = null;
        try{
            message = messageService.getMessageById(id);
            messageService.deleteMessageById(id);
        }catch (Exception e){
            logger.error("删除某条站内信失败"+e.getMessage());
        }
        return "redirect:/msg/detail?conversationId="+message.getConversationId();
    }

    /*怪不得在页面上点不能删除站内信，原来是没有实现，不是删除，不是删除，不是删除！！！，这是设置会话已读*/
    @RequestMapping(value = {"/msg/hasRead"},method = {RequestMethod.GET})
    public String updateMessageHasRead(@RequestParam("conversationId") String conversationId){
        try {
            messageService.updateMessageHasRead(1,conversationId);

        }catch (Exception e){
            logger.error("设置会话已读失败！" +e.getMessage());
        }

        return "redirect:/msg/detail?conversationId=" +conversationId;
    }
}
