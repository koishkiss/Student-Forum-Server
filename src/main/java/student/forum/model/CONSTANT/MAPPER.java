package student.forum.model.CONSTANT;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import student.forum.mapper.SectionJoinMapper;
import student.forum.mapper.SectionMapper;
import student.forum.mapper.UserMapper;

@Component
public class MAPPER implements InitializingBean {
    @Override
    public void afterPropertiesSet() {
        user = userMapper;
        section = sectionMapper;
        section_join = sectionJoinMapper;
    }

    @Autowired
    protected UserMapper userMapper;
    public static UserMapper user;

    @Autowired
    protected SectionMapper sectionMapper;
    public static SectionMapper section;

    @Autowired
    protected SectionJoinMapper sectionJoinMapper;
    public static SectionJoinMapper section_join;

}
