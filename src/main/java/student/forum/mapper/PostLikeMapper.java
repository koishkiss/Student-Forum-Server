package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import student.forum.model.po.PostLike;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface PostLikeMapper extends BaseMapper<PostLike> {

    @Select("SELECT EXISTS(SELECT 1 FROM `post_like` WHERE `uid`=#{uid} AND `post_id`=#{postId})")
    boolean judgeLiked(Integer uid, Integer postId);

    @Insert("INSERT INTO `post_like`(`uid`,`post_id`) VALUES(#{uid},#{postId});" +
            "UPDATE `user` SET `like_num`=`like_num`+1 WHERE `uid`=(SELECT `uid` FROM `post` WHERE `id`=#{postId});" +
            "UPDATE `post` SET `like_num`=`like_num`+1 WHERE `id`=#{postId}"
    )
    void liked(Integer uid, Integer postId);

    @Delete("DELETE FROM `post_like` WHERE `uid`=#{uid} AND `post_id`=#{postId};" +
            "UPDATE `user` SET `like_num`=`like_num`-1 WHERE `uid`=(SELECT `uid` FROM `post` WHERE `id`=#{postId});" +
            "UPDATE `post` SET `like_num`=`like_num`-1 WHERE `id`=#{postId}"
    )
    void disLiked(Integer uid, Integer postId);

    @Select("SELECT " +
                "PL.`post_id` AS `id`," +
                "PL.`like_time` AS `likeTime`," +
                "P.`section_id` AS `sectionId`," +
                "P.`title`," +
                "P.`cover`," +
                "SUBSTRING(P.`content`,1,30) AS `content`," +
                "P.`post_time` AS `postTime`," +
                "P.`view_num` AS `viewNum`," +
                "P.`like_num` AS `likeNum`," +
                "P.`bookmark_num` AS `bookmarkNum`," +
                "P.`comment_num` AS `commentNum`," +
                "P.`status` " +
            "FROM `post_like` PL " +
            "JOIN `post` P " +
            "ON P.`id`=PL.`post_id` " +
            "WHERE PL.`uid`=#{uid} " +
            "LIMIT #{offset},#{pageSize}"
    )
    List<Map<String,Object>> getLikeByUid(int uid, int offset, int pageSize);

    @Select("SELECT " +
            "PL.`post_id` AS `postId`," +
            "PL.`uid` AS `uid`," +
            "PL.`like_time` AS `likeTime`," +
            "SUBSTRING(P.`content`,1,50) AS `content`," +
            "U.`nickname` " +
            "FROM `post_like` PL " +
            "JOIN `post` P ON P.`uid`=#{uid} AND PL.`post_id`=P.`id` " +
            "JOIN `user` U ON U.`uid`=PL.`uid` " +
            "ORDER BY PL.`like_time` DESC " +
            "LIMIT #{pageSize}"
    )
    List<Map<String, Object>> getMyObtainedLikesInFirstTime(Integer uid, Integer pageSize);

    @Select("SELECT " +
                "PL.`post_id` AS `postId`," +
                "PL.`uid` AS `uid`," +
                "PL.`like_time` AS `likeTime`," +
                "SUBSTRING(P.`content`,1,50) AS `content`," +
                "U.`nickname` " +
            "FROM `post_like` PL " +
            "JOIN `post` P ON P.`uid`=#{uid} AND PL.`post_id`=P.`id` " +
            "JOIN `user` U ON U.`uid`=PL.`uid` " +
            "WHERE PL.`like_time`<#{startDate}" +
            "ORDER BY PL.`like_time` DESC " +
            "LIMIT #{pageSize}"
    )
    List<Map<String, Object>> getMyObtainedLikes(Integer uid, Integer pageSize, Date startDate);
}
