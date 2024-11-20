package student.forum.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import student.forum.model.bo.SinglePageSearchBO;
import student.forum.model.po.User;
import student.forum.model.vo.Response;
import student.forum.service.DataService;

import java.util.Date;
import java.util.Map;

@RestController
public class DataController {

    @Resource
    DataService dataService;

    @GetMapping("/popular/active/{top}")
    public Response getPopularActive(@PathVariable(name = "top") Integer top) {
        return dataService.getPopularActive(top);
    }

    @PostMapping("/user/mine/like")
    public Response getLikeMe(
            @RequestAttribute(name = "user") User user,
            @RequestBody SinglePageSearchBO<Map<String,Object>, Date[]> page) {
        return dataService.getMyObtainedLike(user, page);
    }

}
