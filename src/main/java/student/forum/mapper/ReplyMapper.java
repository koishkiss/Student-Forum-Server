package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import student.forum.model.po.Reply;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReplyMapper extends BaseMapper<Reply> {

    @Insert("INSERT INTO `reply`(`comment_id`,`uid`,`content`) VALUES(#{commentId},#{uid},#{content});" +
            "UPDATE `comment` SET `reply_num`=`reply_num`+1 WHERE `id`=#{commentId}"
    )
    void replyComment(Reply reply);

    @Insert("INSERT INTO `reply`(`comment_id`,`uid`,`content`,`call_id`) VALUES(#{commentId},#{uid},#{content},#{callId});" +
            "UPDATE `comment` SET `reply_num`=`reply_num`+1 WHERE `id`=#{commentId}"
    )
    void replyOtherReply(Reply reply);

    @Select("SELECT `nickname` FROM `user` WHERE `uid`=(SELECT `uid` FROM `reply` WHERE `id`=#{replyId})")
    String getReplyMakerNickname(Integer replyId);

    @Select("SELECT `comment_id` FROM `reply` WHERE `id`=#{id}")
    Integer getCommentIdById(Integer id);

    @Select("SELECT " +
                "R.`id`," +
                "R.`comment_id` AS commentId," +
                "R.`uid`," +
                "R.`content`," +
                "R.`call_id` AS callId," +
                "R.`reply_time` AS replyTime," +
                "U.`nickname`," +
                "U.`avatar` " +
            "FROM `reply` R " +
            "JOIN `user` U ON U.`uid`=R.`uid` " +
            "WHERE `comment_id`=#{commentId}"
    )
    List<Map<String,Object>> getAllReplyInComment(Integer commentId);


    @Select("SELECT " +
                "R.`id` AS `replyId`," +
                "R.`comment_id` AS `commentId`," +
                "R.`uid`," +
                "R.`reply_time` AS `releaseTime`," +
                "SUBSTRING(R.`content`,1,50) AS `replyContent`," +
                "SUBSTRING(C.`content`,1,50) AS `myContent`," +
                "U.`nickname` " +
            "FROM `reply` R " +
            "JOIN `comment` C ON R.`comment_id`=C.`id` AND C.`uid`=#{uid} " +
            "JOIN `user` U ON U.`uid`=R.`uid` " +
            "WHERE R.`call_id`=-1 AND R.`uid`<>#{uid} " +
            "ORDER BY R.`reply_time` DESC " +
            "LIMIT #{pageSize}"
    )
    List<Map<String,Object>> getReplyCommentByUidOnFirstTime(Integer uid, Integer pageSize);

    @Select("SELECT " +
                "R.`id` AS `replyId`," +
                "R.`comment_id` AS `commentId`," +
                "R.`uid`," +
                "R.`reply_time` AS `releaseTime`," +
                "SUBSTRING(R.`content`,1,50) AS `replyContent`," +
                "SUBSTRING(C.`content`,1,50) AS `myContent`," +
                "U.`nickname` " +
            "FROM `reply` R " +
            "JOIN `comment` C ON R.`comment_id`=C.`id` AND C.`uid`=#{uid} " +
            "JOIN `user` U ON U.`uid`=R.`uid` " +
            "WHERE R.`reply_time`<#{startDate} AND R.`call_id`=-1 AND R.`uid`<>#{uid} " +
            "ORDER BY R.`reply_time` DESC " +
            "LIMIT #{pageSize}"
    )
    List<Map<String,Object>> getReplyCommentByUid(Integer uid, Integer pageSize, Date startDate);

    @Select("SELECT " +
                "R1.`id` AS `replyId`," +
                "R1.`call_id` AS `callReplyId`," +
                "R1.`uid`," +
                "R1.`reply_time` AS `releaseTime`," +
                "SUBSTRING(R1.`content`,1,50) AS `replyContent`," +
                "SUBSTRING(R2.`content`,1,50) AS `myContent`," +
                "U.`nickname` " +
            "FROM `reply` R1 " +
            "JOIN `reply` R2 ON R1.`id`=R2.`call_id` AND R2.`uid`=#{uid} " +
            "JOIN `user` U ON U.`uid`=R1.`uid` " +
            "WHERE R1.`uid`<>#{uid} " +
            "ORDER BY R1.`reply_time` DESC " +
            "LIMIT #{pageSize}"
    )
    List<Map<String,Object>> getReplyReplyByUidOnFirstTime(Integer uid, Integer pageSize);

    @Select("SELECT " +
                "R1.`id` AS `replyId`," +
                "R1.`call_id` AS `callReplyId`," +
                "R1.`uid`," +
                "R1.`reply_time` AS `releaseTime`," +
                "SUBSTRING(R1.`content`,1,50) AS `replyContent`," +
                "SUBSTRING(R2.`content`,1,50) AS `myContent`," +
                "U.`nickname` " +
            "FROM `reply` R1 " +
            "JOIN `reply` R2 ON R1.`id`=R2.`call_id` AND R2.`uid`=#{uid} " +
            "JOIN `user` U ON U.`uid`=R1.`uid` " +
            "WHERE R1.`reply_time`<#{startDate} R1.`uid`<>#{uid} " +
            "ORDER BY R1.`reply_time` DESC " +
            "LIMIT #{pageSize}"
    )
    List<Map<String,Object>> getReplyReplyByUid(Integer uid, Integer pageSize, Date startDate);

}
