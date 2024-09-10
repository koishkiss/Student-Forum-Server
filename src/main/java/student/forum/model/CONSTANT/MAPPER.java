package student.forum.model.CONSTANT;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import student.forum.mapper.UserMapper;

@Component
public class MAPPER implements InitializingBean {
    @Override
    public void afterPropertiesSet() {
        user = userMapper;
    }

    @Autowired
    protected UserMapper userMapper;
    public static UserMapper user;

}
