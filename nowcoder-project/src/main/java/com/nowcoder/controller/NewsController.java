package com.nowcoder.controller;

import com.nowcoder.dto.CommentVO;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 咨询controller层，重头戏，看@Autowired这么多bean就知道融合了很多功能
 *
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

//    @Autowired//以后可以用到阿里云
//    QiniuService qiniuService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    /*某一条news的详情*/
    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model) {
        News news = newsService.getById(newsId);

        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));

        /*下面讲的是获取当前用户localUserId对某条news的赞踩情况*/
        if (news != null) {
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                model.addAttribute(
                        "like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                model.addAttribute("like", 0);
            }

            // news的评论列表
            List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
            //List<ViewObject> commentVOs = new ArrayList<>();
            List<CommentVO> commentVOs=new ArrayList<>();
            for (Comment comment : comments) {
                //ViewObject vo = new ViewObject();
                CommentVO commentVO=new CommentVO();
                /*获取一条comment*/
                //vo.set("comment", comment);
                commentVO.setComment(comment);
                /*获取这条评论的user*/
                //vo.set("user", userService.getUser(comment.getUserId()));
                commentVO.setUser(userService.getUser(comment.getUserId()));
                //commentVOs.add(vo);
                commentVOs.add(commentVO);
            }
            model.addAttribute("comments", commentVOs);
        }
        return "detail";
    }

    /*获取图片*/
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            /*IO流相关操作，图片文件流-->相应流response.getOutputStream()*/
            StreamUtils.copy(new FileInputStream(new
                    File(ToutiaoUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }

    @RequestMapping(value = "/uploadImage/",method = {RequestMethod.GET})
    public String uploadImage(){
        return "upload";
    }

    /*upload上传图片*/
    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = newsService.saveImage(file);
            //String fileUrl = qiniuService.saveImage(file);七牛云
            //String fileUrl=aliyunService.saveIamge(file);阿里云
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传图片失败");
        }
    }

    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.GET})
    public String addNews(){
        return "addNews";
    }

    /*添加咨询news，应该是先uploadImage，再addNews
    * 这里和github上image的参数不同，MultipartFile*/
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNewsPost(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            if (hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                // 设置一个匿名用户，3是匿名的？
                news.setUserId(3);
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
            //return "redirect:/";
        } catch (Exception e) {
            logger.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布咨询失败");
        }
    }
}
