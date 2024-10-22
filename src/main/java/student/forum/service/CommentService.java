package student.forum.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.CONSTANT.VALUE;
import student.forum.model.ENUM.FileType;
import student.forum.model.po.Comment;
import student.forum.model.vo.CommonErr;
import student.forum.model.vo.Response;
import student.forum.util.FileUtil;
import student.forum.util.HtmlHandleUtil;
import student.forum.util.PageUtil;

import java.util.ArrayList;
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

    public Response getCommentListByPost(int uid, Integer postId,Integer page) {
        page = page < 1 ? 1 : page;
        int commentNum = MAPPER.post.getCommentNumById(postId);
        int pageNum = PageUtil.calculatePageNum(commentNum,VALUE.page_size);
        int offset = PageUtil.calculateOffset(page,VALUE.page_size);

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        class CommentPageVO {
            private List<Map<String,Object>> commentList;
            private int pageNum;
            private int page;
        }

        if (commentNum == 0) {
            return Response.success(new CommentPageVO(new ArrayList<>(),pageNum,page));
        }
        else if (page > pageNum) {
            return Response.failure(CommonErr.THIS_IS_LAST_PAGE);
        }

        List<Map<String,Object>> commentList = MAPPER.comment.selectFromPost(uid, postId, VALUE.page_size, offset);

        int floorId = offset + 2;
        for (Map<String,Object> comment : commentList) {
            comment.put("content", HtmlHandleUtil.escapeToHTML((String) comment.get("content")));
            comment.put("avatarURL", FileUtil.getFileURL((String) comment.get("avatar"), FileType.IMAGE));
            comment.put("floorId",floorId++);
        }

        return Response.success(new CommentPageVO(commentList,pageNum,page));
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
