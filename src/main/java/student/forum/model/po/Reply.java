package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.util.HtmlHandleUtil;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@TableName("reply")
public class Reply {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer commentId;

    private Integer uid;

    private String content;

    private Integer callId;

    private Date replyTime;

    public boolean checkSameCommentWithCallId() {
        return commentId != null && Objects.equals(MAPPER.reply.getCommentIdById(callId), commentId);
    }

    public void setContentAsText() {
        content = HtmlHandleUtil.escapeFromHTML(content);
    }

    public void setContentAsHTML() {
        content = HtmlHandleUtil.escapeToHTML(content);
    }
}
