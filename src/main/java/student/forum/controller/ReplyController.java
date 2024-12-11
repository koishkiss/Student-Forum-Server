package student.forum.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import student.forum.model.po.Reply;
import student.forum.model.po.User;
import student.forum.model.vo.Response;
import student.forum.service.ReplyService;

@RestController
@RequestMapping("/api")
public class ReplyController {

    @Resource
    ReplyService replyService;

    //回复评论
    @PostMapping("/reply/comment")
    public Response replyComment(HttpServletRequest request, @RequestBody Reply reply) {
        return replyService.giveNewReplyOnComment(((User) request.getAttribute("user")).getUid(), reply);
    }

    //回复另一个回复
    @PostMapping("/reply/reply")
    public Response replyOtherReply(HttpServletRequest request, @RequestBody Reply reply) {
        return replyService.giveNewReplyOnOther(((User) request.getAttribute("user")).getUid(), reply);
    }

    //查看某一评论下回复
    @GetMapping("/reply/get")
    public Response getAllReply(@RequestParam("commentId") Integer commentId) {
        return replyService.getAllReply(commentId);
    }

}
