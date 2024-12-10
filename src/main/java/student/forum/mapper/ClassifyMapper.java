package student.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import student.forum.model.po.Classify;

import java.util.List;

@Mapper
public interface ClassifyMapper extends BaseMapper<Classify> {

    @Select("SELECT EXISTS(SELECT 1 FROM `classify` WHERE `id`=#{id})")
    boolean judgeExistsById(Integer id);

    @Select("SELECT EXISTS(SELECT 1 FROM `classify` WHERE `name`=#{name})")
    boolean judgeExistsByName(String name);

}
