package student.forum.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import student.forum.model.po.Post;
import student.forum.model.po.User;
import student.forum.model.vo.Response;
import student.forum.service.PostService;

@RestController
public class PostController {

    @Resource
    PostService postService;

    //发布帖子
    @PostMapping("/post/post")
    public Response postNewPost(
            HttpServletRequest request,
            @RequestBody Post post) {
        return postService.postNewPost(((User) request.getAttribute("user")).getUid(), post);
    }

    //按不同方式检索帖子
    @GetMapping("/section/post/select")
    public Response selectPostsBySectionId(
            HttpServletRequest request,
            @RequestParam(name = "uid",required = false) Integer uid,
            @RequestParam(name = "sectionId",required = false) Integer sectionId,
            @RequestParam(name = "search",required = false) String search,
            @RequestParam(name = "offset",defaultValue = "0") Integer offset) {
        return postService.selectPosts(((User) request.getAttribute("user")).getUid(), uid, sectionId, search, offset);
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
