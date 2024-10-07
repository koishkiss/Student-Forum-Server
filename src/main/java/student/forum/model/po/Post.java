package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@TableName("post")
public class Post {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer sectionId;

    private Integer uid;

    private String title;

    private String cover;

    private String content;

    private Date postTime;

    private Integer viewNum;

    private Integer likeNum;

    private Integer bookmarkNum;

    private Integer commentNum;

    private Integer status;
}
