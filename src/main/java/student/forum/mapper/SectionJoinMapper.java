package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import student.forum.model.po.SectionJoin;

@Mapper
public interface SectionJoinMapper extends BaseMapper<SectionJoin> {

    @Insert("INSERT INTO `section_join`(`section_id`,`uid`) VALUES(#{sectionId},#{uid});" +
            "UPDATE `section` SET `member_num`=`member_num`+1 WHERE `id`=#{sectionId};" +
            "UPDATE `user` SET `join_num`=`join_num`+1 WHERE `uid`=#{uid}")
    void join(int sectionId, int uid);

    @Insert("INSERT INTO `section_join`(`section_id`,`uid`,`authority`) VALUES(#{sectionId},#{uid},#{authority});" +
            "UPDATE `section` SET `member_num`=`member_num`+1 WHERE `id`=#{sectionId};" +
            "UPDATE `user` SET `join_num`=`join_num`+1 WHERE `uid`=#{uid}")
    void join(int sectionId, int uid, int authority);

    @Delete("DELETE FROM `section_join` WHERE `section_id`=#{sectionId},`uid`=#{uid};" +
            "UPDATE `section` SET `member_num`=`member_num`-1 WHERE `id`=#{sectionId};" +
            "UPDATE `user` SET `join_num`=`join_num`-1 WHERE `uid`=#{uid}")
    void cancel(int sectionId, int uid);

    @Select("SELECT EXISTS(SELECT 1 FROM `section_join` WHERE `section_id`=#{sectionId},`uid`=#{uid})")
    boolean judgeExists(int sectionId, int uid);

    @Select("SELECT * FROM `section_join` WHERE `section_id`=#{sectionId},`uid`=#{uid}")
    SectionJoin getInfo(int sectionId, int uid);

}
