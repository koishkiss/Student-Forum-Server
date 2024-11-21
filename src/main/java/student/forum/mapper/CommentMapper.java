package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import student.forum.model.po.Comment;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Insert("INSERT INTO `comment`(`post_id`,`uid`,`content`) VALUES(#{postId},#{uid},#{content});" +
            "UPDATE `post` SET `comment_num`=`comment_num`+1 WHERE `id`=#{postId}"
    )
    void make(Comment comment);

    @Select("SELECT " +
                "C.`id`," +
                "C.`post_id` AS postId," +
                "C.`uid`," +
                "C.`content`," +
                "C.`comment_time` AS commentTime," +
                "C.`like_num` AS likeNum," +
                "C.`reply_num` AS replyNum," +
                "U.`authority`," +
                "U.`nickname`," +
                "U.`avatar` AS avatarURL," +
                "(U.`uid`=P.`uid`) AS isPoster," +
                "(U.`uid`=S.`moderator`) AS isModerator," +
                "CL.`like_time` AS likeTime " +
            "FROM `comment` C " +
            "JOIN `user` U ON C.`uid`=U.`uid` " +
            "JOIN `post` P ON C.`post_id`=P.`id` " +
            "JOIN `section` S ON P.`section_id`=S.`id` " +
            "LEFT JOIN `comment_like` CL ON CL.`comment_id`=C.`id` AND CL.`uid`=#{viewerId} " +
            "WHERE `post_id`=#{postId} " +
            "LIMIT #{offset},#{pageSize}"
    )
    List<Map<String,Object>> selectFromPost(int viewerId, Integer postId, Integer pageSize, Integer offset);

    @Select("SELECT " +
                "C.`id` AS `commentId`," +
                "C.`post_id` AS `postId`," +
                "C.`uid`," +
                "C.`comment_time` AS `releaseTime`," +
                "SUBSTRING(C.`content`,1,50) AS `replyContent`," +
                "SUBSTRING(P.`content`,1,50) AS `myContent`," +
                "U.`nickname` " +
            "FROM `comment` C " +
            "JOIN `post` P ON C.`post_id`=P.`id` AND P.`uid`=#{uid} " +
            "JOIN `user` U ON U.`uid`=C.`uid` AND U.`uid`<>#{uid} " +
            "ORDER BY C.`comment_time` DESC " +
            "LIMIT #{pageSize}"
    )
    List<Map<String,Object>> getCommentByUidOnFirstTime(Integer uid, Integer pageSize);


    @Select("SELECT " +
                "C.`id` AS `commentId`," +
                "C.`post_id` AS `postId`," +
                "C.`uid`," +
                "C.`comment_time` AS `releaseTime`," +
                "SUBSTRING(C.`content`,1,50) AS `replyContent`," +
                "SUBSTRING(P.`content`,1,50) AS `myContent`," +
                "U.`nickname` " +
            "FROM `comment` C " +
            "JOIN `post` P ON C.`post_id`=P.`id` AND P.`uid`=#{uid} " +
            "JOIN `user` U ON U.`uid`=C.`uid` AND U.`uid`<>#{uid} " +
            "WHERE C.`comment_time`<#{startDate} " +
            "ORDER BY C.`comment_time` DESC " +
            "LIMIT #{pageSize}"
    )
    List<Map<String,Object>> getCommentByUid(Integer uid, Integer pageSize, Date startDate);


}
