package student.forum.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.ENUM.FileType;
import student.forum.model.dto.SDULoginData;
import student.forum.model.po.User;
import student.forum.model.vo.CommonErr;
import student.forum.model.vo.Response;
import student.forum.util.CheckUtil;
import student.forum.util.FileUtil;
import student.forum.util.JwtUtil;

@Service
public class UserService {

    public Response login(SDULoginData sduLogin) {
        if (!sduLogin.checkSid()) return Response.failure(400,"错误的学号格式");
        if (!sduLogin.checkPassword()) return Response.failure(400,"请填写密码");

//        User user = MAPPER.user.register(sduLogin.getSid(), BCrypt.hashpw(sduLogin.getPassword(),BCrypt.gensalt()), sduLogin.getSid());
        User user = sduLogin.login();
        if (user == null) {
            return Response.failure(400,"学号或密码错误");
        }
        @Getter
        @Setter
        class Ticket {
            private String token;
            private User user;
            Ticket(User user) {
                this.user = user;
                this.token = JwtUtil.generateToken(user.getUid());
            }
        }

        return Response.success(new Ticket(user));
    }

    public Response updateAvatar(int uid, MultipartFile image) {
        String avatarName = FileUtil.uploadFile(image, FileType.IMAGE);
        MAPPER.user.updateAvatar(uid,avatarName);
        return Response.success(FileUtil.getFileURL(avatarName,FileType.IMAGE));
    }

    public Response updateInformation(int uid, String nickname, String signature) {
        String sql = "";
        boolean first = true;

        if (nickname != null && !nickname.isBlank()) {
            if (CheckUtil.checkSQL(nickname)) {
                return Response.failure(CommonErr.SQL_NOT_ALLOWED_IN_STRING);
            }
            if (MAPPER.user.judgeNicknameExists(nickname)) {
                return Response.failure(400,"该昵称已存在!");
            }
            sql += String.format("`nickname`='%s'",nickname);
            first = false;
        }

        if (signature != null && !signature.isBlank()) {
            if (CheckUtil.checkSQL(signature)) {
                return Response.failure(CommonErr.SQL_NOT_ALLOWED_IN_STRING);
            }
            if (!first) sql += ",";
            else first = false;
            sql += String.format("`signature`='%s'",signature);
        }

        if (!first) {
            MAPPER.user.updateInformation(uid,sql);
        }

        return Response.ok();
    }



}
