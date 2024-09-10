package student.forum.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import student.forum.model.dto.SDULoginData;
import student.forum.model.vo.Response;
import student.forum.service.UserService;

@RestController
public class UserController {

    @Resource
    UserService userService;

    /**/
    @PostMapping("/user/login")
    public Response login(@RequestBody SDULoginData sduLoginData) {
        return userService.login(sduLoginData);
    }

}
