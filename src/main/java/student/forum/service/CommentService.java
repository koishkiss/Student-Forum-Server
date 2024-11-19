package student.forum.service;

import org.springframework.stereotype.Service;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.ENUM.FileType;
import student.forum.model.bo.AllPageSearchBO;
import student.forum.model.po.Comment;
import student.forum.model.vo.AllPageVO;
import student.forum.model.vo.CommonErr;
import student.forum.model.vo.Response;
import student.forum.util.FileUtil;
import student.forum.util.HtmlHandleUtil;

import java.util.List;
import java.util.Map;

@Service
public class CommentService {

    public Response makeNewComment(int uid, Comment comment) {
        comment.setUid(uid);
        comment.setContentAsText();
        MAPPER.comment.make(comment);
        return Response.ok();
    }

    public Response getCommentListByPost(int uid, Integer postId, AllPageSearchBO<Map<String,Object>> pageSearch) {
        List<Map<String,Object>> commentList = pageSearch.doSearch(new AllPageSearchBO.Method<>() {
            @Override
            public Integer getDataNum() {
                return MAPPER.post.getCommentNumById(postId);
            }

            @Override
            public List<Map<String, Object>> getData(Integer offset, Integer pageSize) {
                return MAPPER.comment.selectFromPost(uid, postId, pageSize, offset);
            }
        });

        int floorId = pageSearch.getOffset() + 2;
        for (Map<String,Object> comment : commentList) {
            comment.put("content", HtmlHandleUtil.escapeToHTML(comment.get("content")));
            comment.put("avatarURL", FileUtil.getFileURL((String) comment.get("avatarURL"), FileType.IMAGE));
            comment.put("floorId",floorId++);
        }

        return Response.success(new AllPageVO<>(pageSearch));
    }

    //点赞评论
    public Response likedComment(int viewerUid, int commentId) {
        if (MAPPER.comment_like.judgeLiked(viewerUid,commentId)) return Response.failure(CommonErr.OPERATE_REPEAT);
        MAPPER.comment_like.liked(viewerUid,commentId);
        return Response.ok();
    }

    //取消点赞
    public Response disLikedComment(int viewerUid, int commentId) {
        if (MAPPER.comment_like.judgeLiked(viewerUid,commentId)) {
            MAPPER.comment_like.disLiked(viewerUid,commentId);
            return Response.ok();
        }
        return Response.failure(CommonErr.OPERATE_REPEAT);
    }

}
