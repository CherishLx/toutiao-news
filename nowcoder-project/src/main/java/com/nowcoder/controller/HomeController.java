package com.nowcoder.controller;

import com.nowcoder.dto.ViewResult;
import com.nowcoder.model.*;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 首页controller层
 */
@Controller
public class HomeController {
    private Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    /*//把发送邮件的功能单独建立一个类实现
    @Autowired
    MailSender mailSender;*/

    private List<ViewResult> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewResult> vos = new ArrayList<>();
        for (News news : newsList) {
            //ViewObject vo = new ViewObject();
            ViewResult vo = new ViewResult();
            //vo.set("news", news);
            vo.setNews(news);
            //vo.set("user", userService.getUser(news.getUserId()));
            vo.setUser(userService.getUser(news.getUserId()));
            if (localUserId != 0) {
                //vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
                vo.setLike(likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                //vo.set("like", 0);
                vo.setLike(0);
            }
            vos.add(vo);
        }
        return vos;
    }

    /*注意一下这里和前面的返回类型不同，ViewResult和ViewObject，
    其实差不多，只是ViewResult用到了DTO，可以把所有的ViewObject改成ViewResult*/
    private List<ViewResult> getNewsDto(int offset, int limit){
        // List<News> newsList = newsService.getLatestNews(userId,offset,limit);
        List<News> newsList = newsService.getLatestNews(offset,limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewResult> vos = new ArrayList<>();
        for(News news : newsList){
            ViewResult vo = new ViewResult();
            vo.setNews(news);
            vo.setUser(userService.getUser(news.getUserId()));
            /*这个setLike是获取localuser对这条news的赞踩的状态*/
            if(localUserId != 0){
                vo.setLike(likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            }else {
                vo.setLike(0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getNewsDto(0,30));
        model.addAttribute("user",hostHolder.getUser());
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }

    /*回到首页*/
    @RequestMapping(value = "/test" ,method = RequestMethod.GET)
    public ModelAndView home(ModelMap modelMap){

        logger.info("url=\"home ...回到首页...");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        modelMap.addAttribute("name","loneBoy");
        modelMap.addAttribute("today",sdf.format(date));
        User user = new User();
        user.setId(3);
        user.setName("test");
        user.setPassword("1234");
        modelMap.addAttribute("user",user);
        return new ModelAndView("index");
    }

}
