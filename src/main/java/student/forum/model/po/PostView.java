package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@TableName("post_view")
public class PostView {
    private Integer postId;
    private Integer uid;
    private Date viewTime;
}
