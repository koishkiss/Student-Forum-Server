package student.forum.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import student.forum.model.bo.AllPageSearchBO;
import student.forum.model.po.Comment;
import student.forum.model.po.User;
import student.forum.model.vo.Response;
import student.forum.service.CommentService;

import java.util.Map;

@RestController
public class CommentController {

    @Resource
    CommentService commentService;

    //发布评论
    @PostMapping("/comment/make")
    public Response makeNewComment(HttpServletRequest request, @RequestBody Comment comment) {
        return commentService.makeNewComment(((User) request.getAttribute("user")).getUid(), comment);
    }

    //按帖子获取评论
    @PostMapping("/comment/get")
    public Response getCommentListByPost(
            @RequestAttribute(name = "user") User user,
            @RequestParam(name = "postId") Integer postId,
            @RequestBody AllPageSearchBO<Map<String,Object>> pageSearch) {
        return commentService.getCommentListByPost(user.getUid(), postId, pageSearch);
    }

    //点赞评论
    @GetMapping("/comment/like")
    public Response likePost(
            HttpServletRequest request,
            @RequestParam("commentId") Integer commentId) {
        return commentService.likedComment(((User) request.getAttribute("user")).getUid(), commentId);
    }

    //取消点赞评论
    @GetMapping("/comment/disLike")
    public Response disLikePost(
            HttpServletRequest request,
            @RequestParam("commentId") Integer commentId) {
        return commentService.disLikedComment(((User) request.getAttribute("user")).getUid(), commentId);
    }


}
