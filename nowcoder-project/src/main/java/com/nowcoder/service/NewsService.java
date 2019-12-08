package com.nowcoder.service;

import com.nowcoder.model.News;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * newsService层
 */
public interface NewsService {

    List<News> getLatestNews(int userid, int offset, int limit);

    int addNews(News news);

    News getById(int newsId);

    String saveImage(MultipartFile file) throws IOException;

    int updateCommentCount(int id, int count);

    int updateLikeCount(int id, int count);

    /*获取最新的news*/
    List<News> getLatestNews(int offset, int limit);
}