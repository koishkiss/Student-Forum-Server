package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import student.forum.model.po.Post;
import student.forum.model.po.Section;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper extends BaseMapper<Post> {

    //发布帖子
    @Insert("INSERT INTO `post`(" +
                "`section_id`," +
                "`uid`," +
                "`title`," +
                "`cover`," +
                "`content`" +
            ") " +
            "VALUES (" +
                "#{sectionId}," +
                "#{uid}," +
                "#{title}," +
                "#{cover}," +
                "#{content}" +
            ");" +
            "UPDATE `section` SET `post_num`=`post_num`+1 WHERE `id`=#{sectionId};" +
            "UPDATE `user` SET `post_num`=`post_num`+1 WHERE `uid`=#{uid}"
    )
    void post(Post post);

    //条件获取帖子
    @Select("SELECT " +
                "P.`id`," +
                "P.`section_id` AS `sectionId`," +
                "P.`uid`," +
                "P.`title`," +
                "P.`cover`," +
                "SUBSTRING(P.`content`,1,30) AS `content`," +
                "P.`post_time` AS `postTime`," +
                "P.`view_num` AS `viewNum`," +
                "P.`like_num` AS `likeNum`," +
                "P.`bookmark_num` AS `bookmarkNum`," +
                "P.`comment_num` AS `commentNum`," +
                "P.`status`," +
                "U.`nickname`," +
                "U.`avatar`," +
                "PL.`like_time`," +
                "PB.`mark_time` " +
            "FROM `post` P " +
            "JOIN `user` U ON P.`uid`=U.`uid` " +
            "LEFT JOIN `post_like` PL ON PL.`post_id`=P.`id` AND PL.`uid`=#{uid} " +
            "LEFT JOIN `post_bookmark` PB ON PB.`post_id`=P.`id` AND PB.`uid`=#{uid} " +
            "WHERE ${sql} " +
            "LIMIT #{offset},#{pageSize}"
    )
    List<Map<String,Object>> search(int uid, String sql, int pageSize, int offset);

    //获取帖子内容
    @Select("SELECT * FROM `post` WHERE `id`=#{id}")
    Post getById(int id);

    //查看帖子是否加精
    @Select("SELECT EXISTS(SELECT 1 FROM `post` WHERE `id`=#{id} AND `status`=1)")
    boolean judgeSelected(int id);

    //设置帖子精选状态
    @Update("UPDATE `post` SET `status`=#{status} WHERE `id`=#{id}")
    void setSelected(int id,int status);


}
