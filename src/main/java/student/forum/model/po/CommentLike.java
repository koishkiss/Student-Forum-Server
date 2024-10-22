package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@TableName("comment_like")
public class CommentLike {
    private Integer commentId;
    private Integer uid;
    private Date likeTime;
}
