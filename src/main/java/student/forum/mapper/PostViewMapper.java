package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import student.forum.model.po.PostView;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostViewMapper extends BaseMapper<PostView> {

    @Select("SELECT EXISTS(SELECT 1 FROM `post_view` WHERE `uid`=#{uid} AND `post_id`=#{postId})")
    boolean judgeViewed(Integer uid, Integer postId);

    @Insert("INSERT INTO `post_view`(`uid`,`post_id`) VALUES(#{uid},#{postId});" +
            "UPDATE `post` SET `view_num`=`view_num`+1 WHERE `id`=#{postId}"
    )
    void viewed(Integer uid, Integer postId);

    @Select("SELECT " +
                "PV.`post_id` AS `postId`," +
                "PV.`view_time` AS `viewTime`," +
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
            "FROM `post_view` PV " +
            "JOIN `post` P " +
            "ON P.`id`=PV.`post_id` " +
            "WHERE PV.`uid`=#{uid} " +
            "LIMIT #{offset},#{pageSize}"
    )
    List<Map<String,Object>> getViewByUid(int uid, int offset, int pageSize);

}
