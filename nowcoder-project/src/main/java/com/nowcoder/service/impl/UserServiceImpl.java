package com.nowcoder.service.impl;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 *
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    /*用户首先注册，然后返回loginTicket，map.put("ticket", ticket)*/
    @Override
    public Map<String, Object> register(String username, String password) {
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if(user != null){
            map.put("msg","用户名已经被注册了");
            return  map;
        }

        //密码强度
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        // 登陆，在服务器添加一个loginTicket，并返回给客户端这个loginTicket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    /*登陆，返回loginTicket*/
    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        /*首先去数据库中查找用户user*/
        User user = userDAO.selectByName(username);

        if(user == null){
            map.put("msg","用户名不存在");
            return  map;
        }

        if(!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码不正确");
            return  map;
        }

        map.put("userId",user.getId());

        /*每一次成功登陆，都将添加并返回一个loginTicket*/
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return  map;
    }

    /*添加并返回一个loginTicket*/
    @Override
    public String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replace("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    @Override
    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    /*登出，设置loginTicket的status为1，置为无效*/
    @Override
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket,1);
    }


}
