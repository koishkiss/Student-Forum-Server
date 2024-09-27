package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import student.forum.model.ENUM.FileType;
import student.forum.util.FileUtil;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@TableName("section")
public class Section {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String icon;

    private String slogan;

    private Integer memberNum;

    private Integer postNum;

    private Integer classify;

    private Integer moderator = -1;

    private String admin;

    private Date createTime;

    @JsonIgnore
    public String getIconURL() {
        return FileUtil.getFileURL(icon, FileType.IMAGE);
    }

}
