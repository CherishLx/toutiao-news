package com.nowcoder.dao;

import com.nowcoder.model.News;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 咨询DAO层
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /*添加news*/
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    /*根据id获取news*/
    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    /*更新评论数量*/
    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    /*更新赞踩数量*/
    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);

    /*根据用户id和偏移量offset，？用于分页查询用户的咨询*/
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);

    /*这个只是根据偏移量和数量查询*/
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,
            " order by created_date desc limit #{offset},#{limit}" })
    List<News> selectNewsByOffset( @Param("offset") int offset, @Param("limit") int limit);
}
