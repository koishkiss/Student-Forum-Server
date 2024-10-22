package student.forum.service;

import org.springframework.stereotype.Service;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.CONSTANT.VALUE;
import student.forum.model.ENUM.FileType;
import student.forum.model.po.Post;
import student.forum.model.po.User;
import student.forum.model.vo.CommonErr;
import student.forum.model.vo.Response;
import student.forum.util.ConditionalSqlMaker;
import student.forum.util.FileUtil;
import student.forum.util.HtmlHandleUtil;

import java.util.List;
import java.util.Map;

@Service
public class PostService {

    //发布帖子
    public Response postNewPost(int uid,Post post) {
        if (MAPPER.section_join.judgeExists(post.getSectionId(),uid)) {
            post.setUid(uid);
            post.setContentAsText();
            System.out.println(post.getContent());
            MAPPER.post.post(post);
            return Response.ok();
        } else {
            return Response.failure(400,"请先关注该板块!");
        }
    }

    //条件获取帖子
    public Response selectPosts(int searcherUid, Integer uid, Integer sectionId, String search, Integer offset) {
        ConditionalSqlMaker sqlMaker = new ConditionalSqlMaker();
        String sql = sqlMaker.addCondition("U.`uid`",uid)
                            .addCondition("P.`section_id`",sectionId)
                            .addSearchCondition("P.`title`",search)
                            .getConditionSql();

        List<Map<String,Object>> postList = MAPPER.post.search(searcherUid, sql, VALUE.page_size, offset);

        if (postList.isEmpty()) return Response.failure(CommonErr.NO_DATA);

        for (Map<String,Object> post : postList) {
            post.put("content", HtmlHandleUtil.escapeToHTML((String) post.get("content")));
            if (post.get("cover") != null) {
                post.put("coverURL", FileUtil.getFileURL((String) post.get("cover"), FileType.IMAGE));
            }
            post.put("avatarURL",FileUtil.getFileURL((String) post.get("avatar"),FileType.IMAGE));
        }

        return Response.success(postList);
    }

    //获取某用户喜欢的\收藏的\看过的的帖子
    public Response selectPosts(Integer uid, Integer offset, int operator) {
        List<Map<String,Object>> postList = switch (operator) {
            case 1 -> MAPPER.post_like.getLikeByUid(uid,offset,VALUE.page_size);
            case 2 -> MAPPER.post_mark.getBookmarkByUid(uid,offset,VALUE.page_size);
            case 3 -> MAPPER.post_view.getViewByUid(uid,offset,VALUE.page_size);
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };

        if (postList.isEmpty()) return Response.failure(CommonErr.NO_DATA);

        for (Map<String,Object> post : postList) {
            post.put("content", HtmlHandleUtil.escapeToHTML((String) post.get("content")));
            if (post.get("cover") != null) {
                post.put("coverURL", FileUtil.getFileURL((String) post.get("cover"), FileType.IMAGE));
            }
        }

        return Response.success(postList);
    }

    //查看某一帖子
    public Response getPost(int viewerUid, int postId) {
        Post post = MAPPER.post.getById(postId);
        if (post == null) return Response.failure(CommonErr.NO_DATA);

        post.addViews(viewerUid);

        return Response.success(post.toReturnMap(viewerUid));
    }

    //点赞帖子
    public Response likedPost(int viewerUid, int postId) {
        if (MAPPER.post_like.judgeLiked(viewerUid,postId)) return Response.failure(CommonErr.OPERATE_REPEAT);
        MAPPER.post_like.liked(viewerUid,postId);
        return Response.ok();
    }

    //取消点赞
    public Response disLikedPost(int viewerUid, int postId) {
        if (MAPPER.post_like.judgeLiked(viewerUid,postId)) {
            MAPPER.post_like.disLiked(viewerUid,postId);
            return Response.ok();
        }
        return Response.failure(CommonErr.OPERATE_REPEAT);
    }


    //收藏帖子
    public Response markedPost(int viewerUid, int postId) {
        if (MAPPER.post_mark.judgeMarked(viewerUid,postId)) return Response.failure(CommonErr.OPERATE_REPEAT);
        MAPPER.post_mark.marked(viewerUid,postId);
        return Response.ok();
    }


    //取消收藏
    public Response disMarkedPost(int viewerUid, int postId) {
        if (MAPPER.post_mark.judgeMarked(viewerUid,postId)) {
            MAPPER.post_mark.disMarked(viewerUid,postId);
            return Response.ok();
        }
        return Response.failure(CommonErr.OPERATE_REPEAT);
    }


    //帖子加精
    public Response setSelectedPost(User user,int sectionId, int postId, boolean selected) {
        if (!(user.isSuperAdmin() || MAPPER.section.getModeratorBySection(sectionId) == user.getUid())) {
            return Response.failure(CommonErr.NO_AUTHORITY);
        }

        if (MAPPER.post.judgeSelected(postId) == selected) {
            return Response.failure(CommonErr.OPERATE_REPEAT);
        }

        MAPPER.post.setSelected(postId,selected ? 1 : 0);
        return Response.ok();
    }


}
