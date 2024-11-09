package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import student.forum.model.dto.JoinedSectionDTO;
import student.forum.model.po.Section;

import java.util.List;

@Mapper
public interface SectionMapper extends BaseMapper<Section> {

    //创建版块信息
    @Insert("INSERT INTO `section`(" +
                "`name`," +
                "`icon`," +
                "`slogan`," +
                "`classify`," +
                "`moderator`" +
            ") " +
            "VALUES (" +
                "#{name}," +
                "#{icon}," +
                "#{slogan}," +
                "#{classify}," +
                "#{moderator}" +
            ")"
    )
    void create(Section section);

    //查看该名字的版块是否存在
    @Select("SELECT EXISTS(SELECT 1 FROM `section` WHERE `name`=#{name})")
    boolean judgeNameExists(String name);

    //根据版块名获取版块id
    @Select("SELECT `id` FROM `section` WHERE `name`=#{name}")
    int getIdByName(String name);

    //更新版头
    @Update("UPDATE `section` SET `icon`=#{icon} WHERE `id`=#{id}")
    void updateIcon(int id, String icon);

    //更新信息
    @Update("UPDATE `section` SET ${sql} WHERE `id`=#{id}")
    void updateInformation(int id, String sql);

    //更新管理员
    @Update("UPDATE `section` SET `admin`=#{adminList} WHERE `id`=#{id}")
    void updateAdminList(int id, String adminList);

    @Select("SELECT S.*,SJ.*,C.`name` AS `classifyName` FROM `section` S " +
            "JOIN `section_join` SJ ON SJ.`uid`=#{uid} AND S.`id`=SJ.`section_id` " +
            "JOIN `classify` C ON S.`classify`=C.`id`"
    )
    List<JoinedSectionDTO> selectJoinedSectionByUid(int uid);

    //获取版主信息
    @Select("SELECT `moderator` FROM `section` WHERE `id`=#{id}")
    int getModeratorBySection(int id);

    //获取信息
    @Select("SELECT * FROM `section` WHERE `id`=#{id}")
    Section getInformation(int id);

}
