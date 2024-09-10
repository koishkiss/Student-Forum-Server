package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

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

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password,this.password);
    }

}
