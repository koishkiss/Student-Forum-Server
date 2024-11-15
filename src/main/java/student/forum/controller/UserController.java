package student.forum.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import student.forum.model.dto.SDULoginData;
import student.forum.model.po.User;
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
    public Response getMyInfo(@RequestAttribute User user) {
        return Response.success(user);
    }

    //用户更新头像
    @PutMapping("/user/update/avatar")
    public Response updateAvatar(
            HttpServletRequest request,
            @RequestParam(name = "image") MultipartFile image) {
        return userService.updateAvatar(request.getIntHeader("uid"),image);
    }

    //更新用户信息
    @PutMapping("/user/update/information")
    public Response updateInformation(
            HttpServletRequest request,
            @RequestParam(name = "nickname", required = false) String nickname,
            @RequestParam(name = "signature", required = false) String signature){
        return userService.updateInformation(request.getIntHeader("uid"),nickname,signature);
    }

    //获取某一用户信息
    @GetMapping("/user/other/info")
    public Response getOtherPersonInfo(@RequestParam(name = "uid") Integer uid) {
        return userService.getOtherPersonInfo(uid);
    }

}
