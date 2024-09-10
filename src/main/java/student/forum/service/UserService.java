package student.forum.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import student.forum.model.dto.SDULoginData;
import student.forum.model.po.User;
import student.forum.model.vo.Response;
import student.forum.util.JwtUtil;

@Service
public class UserService {

    public Response login(SDULoginData sduLogin) {
        if (!sduLogin.checkSid()) return Response.failure(400,"错误的学号格式");
        if (!sduLogin.checkPassword()) return Response.failure(400,"请填写密码");

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

}
