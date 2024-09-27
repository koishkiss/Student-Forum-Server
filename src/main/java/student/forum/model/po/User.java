package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;
import student.forum.model.CONSTANT.VALUE;
import student.forum.model.ENUM.FileType;
import student.forum.util.FileUtil;

@Getter
@Setter
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(type= IdType.AUTO)
    private Integer uid;

    private String sid;

    @JsonIgnore
    private String password;

    private Integer authority;

    private String realName;

    private String nickname;

    @JsonIgnore
    private String avatar;

    private String signature;

    @SuppressWarnings("unused")
    public String getAvatarURL() {
        return FileUtil.getFileURL(avatar, FileType.IMAGE);
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password,this.password);
    }

    @JsonIgnore
    public boolean isLocked() {
        return authority == 0;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return authority >= 2;
    }

    @JsonIgnore
    public boolean isSuperAdmin() {
        return authority == 3;
    }

}
