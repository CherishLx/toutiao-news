package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 登录controller层，主要负责注册，登录，登出
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    /*注册register*/
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    /*@ResponseBody*/
    public String reg(Model model,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "rememberme", defaultValue = "1") boolean rememberme,
            HttpServletResponse response) {
        logger.info("/reg/ " + username + ":进行了注册");

        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                //可在同一应用服务器内共享方法：设置cookie.setPath("/")
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                /*return ToutiaoUtil.getJSONString(0, "注册成功");*/
                return "redirect:/";
            } else {
                /*return ToutiaoUtil.getJSONString(1, map);*/
                /*model.addAttribute("error","注册失败，请重新注册");
                return "redirect:/register";*/
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            /*return ToutiaoUtil.getJSONString(1,"注册异常");*/
            /*model.addAttribute("error","注册异常");
            return "redirect:/register";*/
            model.addAttribute("msg","注册异常，请重试");
            return "login";
        }
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    /*@ResponseBody*/
    public String login(Model model,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
            HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            /*登录成功的map中含有ticket*/
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                /*设置cookie在所有路径下"/"获取*/
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

                eventProducer.fireEvent(
                        new EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
                        .setExt("username", "头条咨询").setExt("to", "545212110@qq.com"));
                /*return ToutiaoUtil.getJSONString(0, "成功");*/
                return "redirect:/";
            } else {
                /*return ToutiaoUtil.getJSONString(1,map);*/
                /*model.addAttribute("error","登录失败,请重新登录!");
                return "redirect:/login";*/
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            /*return ToutiaoUtil.getJSONString(1,"登录异常");*/
            /*model.addAttribute("error","登录异常");
            return "redirect:/login";*/
            model.addAttribute("msg","登陆异常，请重试");
            return "login";
        }
    }

    /*跳转到注册登陆页面*/
    @RequestMapping(path = {"/relogin"}, method = RequestMethod.GET)
    public String relogin(Model model, @RequestParam(value = "next", defaultValue = "", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    /*logout登出*/
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        logger.info("logout：跳转到首页");
        return "redirect:/";
    }

}
