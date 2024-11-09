package student.forum.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import student.forum.model.ENUM.FileType;
import student.forum.util.FileUtil;

import java.util.Date;

@Setter
@NoArgsConstructor
public class JoinedSectionDTO {

    @Getter
    private Integer sectionId;

    @Getter
    private Integer uid;

    @Getter
    private Integer identity;

    @Getter
    private String name;

    private String icon;

    @Getter
    private Integer memberNum;

    @Getter
    private Integer postNum;

    @Getter
    private Integer classify;

    @Getter
    private String classifyName;

    @Getter
    private Date createTime;

    @SuppressWarnings("unused")
    public String getIconURL() {
        return FileUtil.getFileURL(icon, FileType.IMAGE);
    }
}
