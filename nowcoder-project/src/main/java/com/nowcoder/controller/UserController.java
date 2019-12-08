package com.nowcoder.controller;

import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试controller类
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/test")
    public String test(){
        return "hello,test!";
    }

    @RequestMapping(value = "/find")
    User findUser(@RequestParam int id){
        return userDAO.selectById(id);
    }


}