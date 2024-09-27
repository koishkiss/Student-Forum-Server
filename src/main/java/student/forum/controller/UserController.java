package student.forum.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import student.forum.model.dto.SDULoginData;
import student.forum.model.vo.Response;
import student.forum.service.UserService;

@RestController
public class UserController {

    @Resource
    UserService userService;

    //用户登入
    @PostMapping("/user/login")
    public Response login(@RequestBody SDULoginData sduLoginData) {
        return userService.login(sduLoginData);
    }

    //用户个人信息
    @GetMapping("/user/mine/info")
    public Response getMyInfo(HttpServletRequest request) {
        return Response.success(request.getAttribute("user"));
    }

    //用户更新头像
    @PostMapping("/user/update/avatar")
    public Response updateAvatar(
            HttpServletRequest request,
            @RequestParam(name = "image") MultipartFile image) {
        return userService.updateAvatar(request.getIntHeader("uid"),image);
    }

    //更新用户信息
    @GetMapping("/user/update/information")
    public Response updateInformation(
            HttpServletRequest request,
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "signature") String signature){
        return userService.updateInformation(request.getIntHeader("uid"),nickname,signature);
    }

}
