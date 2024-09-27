package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@TableName("section_join")
public class SectionJoin {
    private Integer sectionId;

    private Integer uid;

    private Integer identity;

    private Date joinTime;

}
