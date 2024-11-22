package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostBookmarkMapper extends BaseMapper<PostBookmarkMapper> {

    @Select("SELECT EXISTS(SELECT 1 FROM `post_bookmark` WHERE `uid`=#{uid} AND `post_id`=#{postId})")
    boolean judgeMarked(Integer uid, Integer postId);

    @Insert("INSERT INTO `post_bookmark`(`uid`,`post_id`) VALUES(#{uid},#{postId});" +
            "UPDATE `user` SET `bookmark_num`=`bookmark_num`+1 WHERE `uid`=#{uid};" +
            "UPDATE `post` SET `bookmark_num`=`bookmark_num`+1 WHERE `id`=#{postId}"
    )
    void marked(Integer uid, Integer postId);

    @Delete("DELETE FROM `post_bookmark` WHERE `uid`=#{uid} AND `post_id`=#{postId};" +
            "UPDATE `user` SET `bookmark_num`=`bookmark_num`-1 WHERE `uid`=#{uid};" +
            "UPDATE `post` SET `bookmark_num`=`bookmark_num`-1 WHERE `id`=#{postId}"
    )
    void disMarked(Integer uid, Integer postId);

    @Select("SELECT " +
                "PB.`post_id` AS `id`," +
                "PB.`mark_time` AS `markTime`," +
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
            "FROM `post_bookmark` PB " +
            "JOIN `post` P " +
            "ON P.`id`=PB.`post_id` " +
            "WHERE PB.`uid`=#{uid} " +
            "ORDER BY PB.`mark_time` DESC " +
            "LIMIT #{offset},#{pageSize}"
    )
    List<Map<String,Object>> getBookmarkByUid(int uid, int offset, int pageSize);

}
