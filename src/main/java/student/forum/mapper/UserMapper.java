package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import student.forum.model.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("INSERT INTO `user`(`sid`,`password`,`real_name`) VALUES (#{sid},#{password},#{realName});" +
            "SELECT * FROM `user` WHERE `sid`=#{sid}"
    )
    User register(String sid,String password,String realName);

    @Select("SELECT * FROM `user` WHERE `sid`=#{sid}")
    User selectUserBySid(String sid);

}
