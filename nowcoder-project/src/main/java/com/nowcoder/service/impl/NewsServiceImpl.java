package com.nowcoder.service.impl;

import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.News;
import com.nowcoder.service.NewsService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDAO newsDAO;

    /*分页查询某个用户的最近的news*/
    @Override
    public List<News> getLatestNews(int userid, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userid,offset,limit);
    }

    /*获取最新的news*/
    @Override
    public List<News> getLatestNews(int offset, int limit) {
        return newsDAO.selectNewsByOffset(offset,limit);
    }

    @Override
    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    /*通过ID查询news*/
    @Override
    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

    /*保存news图片*/
    @Override
    public String saveImage(MultipartFile file) throws IOException {
        //xxx. = a.jpg
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if(dotPos < 0){
            return  null;
        }
        /*fileExt是文件的后缀名,"png", "bmp", "jpg", "jpeg"*/
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt)){
            return null;
        }
        /*文件名fileName*/
        String fileName = UUID.randomUUID().toString().replaceAll("-","") + "."+fileExt;
        /*文件拷贝，io流*/
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        /*返回的是网址+参数*/
        return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name=" + fileName;
    }

    /*更新评论数量*/
    @Override
    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id,count);
    }

    /*更新赞踩的数量*/
    @Override
    public int updateLikeCount(int id, int count) {
        return newsDAO.updateLikeCount(id,count);
    }

}
