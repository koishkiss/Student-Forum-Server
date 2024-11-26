package student.forum.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import student.forum.model.bo.AllPageSearchBO;
import student.forum.model.bo.SinglePageSearchBO;
import student.forum.model.po.Post;
import student.forum.model.po.User;
import student.forum.model.vo.CommonErr;
import student.forum.model.vo.Response;
import student.forum.service.PostService;

import java.util.Map;

@RestController
public class PostController {

    @Resource
    PostService postService;

    //发布帖子
    @PostMapping("/post/post")
    public Response postNewPost(
            HttpServletRequest request,
            @RequestBody Post post) {
        if (post.getTitle().isBlank()) {
            return Response.failure(CommonErr.PARAM_WRONG.setMsg("标题不可为空!"));
        }
        if (post.getContent().isBlank()) {
            return Response.failure(CommonErr.PARAM_WRONG.setMsg("内容不可为空!"));
        }
        return postService.postNewPost(((User) request.getAttribute("user")).getUid(), post);
    }

    //获取推荐的帖子(实际倒序获取)
    @PostMapping("/post/get/recommend")
    public Response getRecommendPost(
            @RequestAttribute(name = "user") User user,
            @RequestBody SinglePageSearchBO<Map<String,Object>,Integer> postPage) {
        return postService.getNewPosts(user, postPage);
    }

    //按版块分页获取帖子
    @PostMapping("/post/get")
    public Response selectPostsBySectionId(
            @RequestAttribute(name = "user") User user,
            @RequestParam(name = "sectionId") Integer sectionId,
            @RequestParam(name = "onlySelected", defaultValue = "false") Boolean onlySelected,
            @RequestBody AllPageSearchBO<Map<String,Object>> pageSearch) {
        return postService.selectPostsBySectionId(user.getUid(), sectionId, onlySelected, pageSearch);
    }

    //按不同方式检索帖子
    @GetMapping("/post/get")
    public Response selectPosts(
            @RequestAttribute(name = "user") User user,
            @RequestParam(name = "uid",required = false) Integer uid,
            @RequestParam(name = "sectionId",required = false) Integer sectionId,
            @RequestParam(name = "search",required = false) String search,
            @RequestParam(name = "offset",defaultValue = "0") Integer offset,
            @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) {
        return postService.selectPosts(user.getUid(), uid, sectionId, search, offset, pageSize);
    }

    //获取某用户喜欢的\收藏的\看过的帖子
    @GetMapping("/post/get/{type}")
    public Response selectPostsLiked(
            @RequestParam(name = "uid") Integer uid,
            @RequestParam(name = "offset") Integer offset,
            @PathVariable(name = "type") String type) {
        return switch (type) {
            case "liked" -> postService.selectPosts(uid, offset, 1);
            case "marked" -> postService.selectPosts(uid, offset, 2);
            case "viewed" -> postService.selectPosts(uid, offset, 3);
            default -> Response.failure(CommonErr.CONTENT_NOT_FOUND);
        };
    }

    //查看某一帖子
    @GetMapping("/post/view")
    public Response viewPost(
            HttpServletRequest request,
            @RequestParam(name = "postId") Integer postId) {
        return postService.getPost(((User) request.getAttribute("user")).getUid(), postId);
    }

    //点赞帖子
    @GetMapping("/post/like")
    public Response likePost(
            HttpServletRequest request,
            @RequestParam("postId") Integer postId) {
        return postService.likedPost(((User) request.getAttribute("user")).getUid(), postId);
    }

    //取消点赞
    @GetMapping("/post/disLike")
    public Response disLikePost(
            HttpServletRequest request,
            @RequestParam("postId") Integer postId) {
        return postService.disLikedPost(((User) request.getAttribute("user")).getUid(), postId);
    }

    //收藏帖子
    @GetMapping("/post/mark")
    public Response markPost(
            HttpServletRequest request,
            @RequestParam("postId") Integer postId) {
        return postService.markedPost(((User) request.getAttribute("user")).getUid(), postId);
    }

    //取消收藏
    @GetMapping("/post/disMark")
    public Response disMarkPost(
            HttpServletRequest request,
            @RequestParam("postId") Integer postId) {
        return postService.disMarkedPost(((User) request.getAttribute("user")).getUid(), postId);
    }

    //帖子加精
    @GetMapping("/post/select")
    public Response markPost(
            HttpServletRequest request,
            @RequestParam("sectionId") Integer sectionId,
            @RequestParam("postId") Integer postId) {
        return postService.setSelectedPost((User) request.getAttribute("user"), sectionId, postId, true);
    }

    //取消加精
    @GetMapping("/post/disSelect")
    public Response disMarkPost(
            HttpServletRequest request,
            @RequestParam("sectionId") Integer sectionId,
            @RequestParam("postId") Integer postId) {
        return postService.setSelectedPost((User) request.getAttribute("user"), sectionId, postId, false);
    }



}
