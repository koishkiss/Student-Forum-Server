package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import student.forum.model.po.CommentLike;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLike> {


    @Select("SELECT EXISTS(SELECT 1 FROM `comment_like` WHERE `uid`=#{uid} AND `comment_id`=#{commentId})")
    boolean judgeLiked(Integer uid, Integer commentId);

    @Insert("INSERT INTO `comment_like`(`comment_id`,`uid`) VALUES(#{commentId},#{uid});" +
            "UPDATE `comment` SET `like_num`=`like_num`+1 WHERE `id`=#{commentId};" +
            "UPDATE `user` SET `like_num`=`like_num`+1 WHERE `uid`=(SELECT `uid` FROM `comment` WHERE `id`=#{commentId})"
    )
    void liked(Integer uid, Integer commentId);

    @Delete("DELETE FROM `comment_like` WHERE `comment_id`=#{commentId} AND `uid`=#{uid};" +
            "UPDATE `comment` SET `like_num`=`like_num`-1 WHERE `id`=#{commentId};" +
            "UPDATE `user` SET `like_num`=`like_num`-1 WHERE `uid`=(SELECT `uid` FROM `comment` WHERE `id`=#{commentId})"
    )
    void disLiked(Integer uid, Integer commentId);

    @Select("SELECT " +
                "CL.`comment_id` AS `commentId`," +
                "CL.`uid` AS `uid`," +
                "CL.`like_time` AS `likeTime`," +
                "SUBSTRING(C.`content`,1,50) AS `content`," +
                "C.`post_id` AS `postId`," +
                "U.`nickname` " +
            "FROM `comment_like` CL " +
            "JOIN `comment` C ON C.`uid`=#{uid} AND CL.`comment_id`=C.`id` " +
            "JOIN `user` U ON U.`uid`=CL.`uid` " +
            "ORDER BY CL.`like_time` DESC " +
            "LIMIT #{pageSize}"
    )
    List<Map<String,Object>> getMyObtainedLikesInFirstTime(Integer uid, Integer pageSize);

    @Select("SELECT " +
                "CL.`comment_id` AS `commentId`," +
                "CL.`uid` AS `uid`," +
                "CL.`like_time` AS `likeTime`," +
                "SUBSTRING(C.`content`,1,50) AS `content`," +
                "C.`post_id` AS `postId`," +
                "U.`nickname` " +
            "FROM `comment_like` CL " +
            "JOIN `comment` C ON C.`uid`=#{uid} AND CL.`comment_id`=C.`id` " +
            "JOIN `user` U ON U.`uid`=CL.`uid` " +
            "WHERE CL.`like_time`<#{startDate}" +
            "ORDER BY CL.`like_time` DESC " +
            "LIMIT #{pageSize}"
    )
    List<Map<String,Object>> getMyObtainedLikes(Integer uid, Integer pageSize, Date startDate);

}
