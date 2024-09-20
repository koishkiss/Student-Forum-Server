package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import student.forum.model.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("INSERT INTO `user`(" +
                "`sid`," +
                "`password`," +
                "`real_name`," +
                "`nickname`" +
            ") " +
            "VALUES (" +
                "#{sid}," +
                "#{password}," +
                "#{realName}," +
                "CONCAT('u_',#{sid})" +
            ");" +
            "SELECT * FROM `user` WHERE `sid`=#{sid}"
    )
    User register(String sid,String password,String realName);

    @Select("SELECT * FROM `user` WHERE `sid`=#{sid}")
    User selectUserBySid(String sid);

    @Select("SELECT * FROM `user` WHERE `uid`=#{uid}")
    User selectUserByUid(int uid);

    @Select("SELECT EXISTS(SELECT 1 FROM `user` WHERE `nickname`=#{nickname})")
    boolean judgeNicknameExists(String nickname);

    @Update("UPDATE `user` SET `avatar`=#{avatarName} WHERE `uid`=#{uid}")
    void updateAvatar(int uid, String avatarName);

    @Update("UPDATE `user` SET ${sql} WHERE `uid`=#{uid}")
    void updateInformation(int uid, String sql);

}
