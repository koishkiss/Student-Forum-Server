package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import student.forum.model.po.Reply;

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

}
