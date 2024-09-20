package student.forum.core.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import student.forum.core.exception.CommonErrException;
import student.forum.core.exception.TokenException;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.po.User;
import student.forum.model.vo.CommonErr;
import student.forum.util.DateUtil;
import student.forum.util.JwtUtil;

import java.util.Arrays;

/*
* 拦截器
* 通过拦截器进行权限管理和访问限制
* */

@Component
public class ConnectInterceptor implements HandlerInterceptor {

    //对任意游客开放的接口
    private static final String[] FOR_TOURIST = {
            "/user/login",
            "/home-page"
    };

    //对管理员开放的接口
    private static final String[] FOR_ADMIN = {
    };

    @Override
    public boolean preHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object Handle) {

        //获取请求地址
        String path = request.getServletPath();

        System.out.println("\n"+ DateUtil.getCurrentTime()+"  new request:"+path+" from "+request.getRemoteAddr());

        //测试未带token时使用
//        if (true) return true;

        //任意游客可访问的接口，无需请求头即可访问
        if (Arrays.stream(FOR_TOURIST).anyMatch(path::startsWith)) {
            return true;
        }

        //检查是否携带token
        String token = request.getHeader("Authorization");
        if (token == null) throw new TokenException("请登入!");

        //检查是否携带uid
        int uid = request.getIntHeader("uid");
        if (uid == -1) throw new TokenException("请登入!");

        //检查uid和token是否匹配
        int claims = JwtUtil.getClaimsByToken(token);
        if (claims == -1) throw new TokenException("登入超时，请重新登入!");
        else if (claims != uid) throw new TokenException("身份错误，请重新登入!");

        //uid和token匹配，允许继续访问
        User user = MAPPER.user.selectUserByUid(uid);
        if (user == null) throw  new CommonErrException(CommonErr.NO_AUTHORITY.setMsg("账号不存在!"));
        request.setAttribute("user",user);

        //账号被封禁
        if (user.isLocked()) throw new CommonErrException(CommonErr.NO_AUTHORITY.setMsg("账号好像被封禁了QWQ"));

        //管理员访问
        if (user.isAdmin()) {
            return true;
        }
        else if (Arrays.stream(FOR_ADMIN).anyMatch(path::startsWith)) {
            throw new CommonErrException(CommonErr.NO_AUTHORITY);
        }

        return true;

    }
}
