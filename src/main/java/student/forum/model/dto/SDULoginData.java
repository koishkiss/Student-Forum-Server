package student.forum.model.dto;

import kong.unirest.Unirest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mindrot.jbcrypt.BCrypt;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.po.User;
import student.forum.util.CheckUtil;

@Getter
@Setter
@NoArgsConstructor
public class SDULoginData {
    String sid;
    String password;

    //检查uid是否正确填写
    public boolean checkSid() {
        return CheckUtil.checkSid(sid);
    }

    //检查password是否正确填写
    public boolean checkPassword() {
        return password != null && !password.isEmpty();
    }

    //统一认证登录
    public User login() {

        //先尝试从数据库获取成员信息
        User user = MAPPER.user.selectUserBySid(sid);

        //如果已经注册过，则检查数据库中密码，返回数据，否则注册
        if (user != null) {
            if (user.checkPassword(password)) return user;
            return null;
        }

        // 获取ticket
        String ticket = Unirest.post("https://pass.sdu.edu.cn/cas/restlet/tickets")
                .body("username=" + sid + "&password=" + password)
                .asString()
                .getBody();

        //如果ticket不以TGT开头，说明登入失败
        if (!ticket.startsWith("TGT")) {
            return null;
        }

        // 获取sTicket
        String sTicket = Unirest.post("https://pass.sdu.edu.cn/cas/restlet/tickets/" + ticket)
                .body("service=https://service.sdu.edu.cn/tp_up/view?m=up")
                .asString()
                .getBody();

        if (!sTicket.startsWith("ST")) {
            return null;
        }

        // 获取姓名和学号
        String validationResult = Unirest.get("https://pass.sdu.edu.cn/cas/serviceValidate")
                .queryString("ticket", sTicket)
                .queryString("service", "https://service.sdu.edu.cn/tp_up/view?m=up")
                .asString()
                .getBody();

        Document document = Jsoup.parse(validationResult);
        String name = document.getElementsByTag("cas:USER_NAME").text();//获取学生姓名

        return MAPPER.user.register(sid, BCrypt.hashpw(password,BCrypt.gensalt()), name);
    }

}
