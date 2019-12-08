package com.nowcoder.service;

import com.nowcoder.model.User;

import java.util.Map;

/**
 * 用户service层
 */
public interface UserService {

    Map<String,Object> register(String username, String password);

    Map<String,Object> login(String username, String password);

    String addLoginTicket(int userId);

    User getUser(int id);

    void logout(String ticket);

}