package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import student.forum.util.HtmlHandleUtil;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@TableName("comment")
public class Comment {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer postId;

    private Integer uid;

    private String content;

    private Date commentTime;

    private Integer likeNum;

    private Integer replyNum;

    public void setContentAsText() {
        content = HtmlHandleUtil.escapeFromHTML(content);
    }

    public void setContentAsHTML() {
        content = HtmlHandleUtil.escapeToHTML(content);
    }

}
