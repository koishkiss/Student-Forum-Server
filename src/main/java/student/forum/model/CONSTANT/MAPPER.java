package student.forum.model.CONSTANT;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import student.forum.mapper.*;

@Component
public class MAPPER implements InitializingBean {
    @Override
    public void afterPropertiesSet() {
        user = userMapper;

        section = sectionMapper;
        classify = classifyMapper;
        section_join = sectionJoinMapper;

        post = postMapper;
        post_view = postViewMapper;
        post_like = postLikeMapper;
        post_mark = postBookmarkMapper;
    }

    @Autowired
    protected UserMapper userMapper;
    public static UserMapper user;

    @Autowired
    protected SectionMapper sectionMapper;
    public static SectionMapper section;

    @Autowired
    protected ClassifyMapper classifyMapper;
    public static ClassifyMapper classify;

    @Autowired
    protected SectionJoinMapper sectionJoinMapper;
    public static SectionJoinMapper section_join;

    @Autowired
    protected PostMapper postMapper;
    public static PostMapper post;

    @Autowired
    protected PostViewMapper postViewMapper;
    public static PostViewMapper post_view;

    @Autowired
    protected PostLikeMapper postLikeMapper;
    public static PostLikeMapper post_like;

    @Autowired
    protected PostBookmarkMapper postBookmarkMapper;
    public static PostBookmarkMapper post_mark;

}
