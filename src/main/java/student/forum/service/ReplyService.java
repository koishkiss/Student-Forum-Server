package student.forum.service;

import org.springframework.stereotype.Service;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.ENUM.FileType;
import student.forum.model.po.Reply;
import student.forum.model.vo.CommonErr;
import student.forum.model.vo.Response;
import student.forum.util.FileUtil;
import student.forum.util.HtmlHandleUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReplyService {

    //回复评论
    public Response giveNewReplyOnComment(int uid, Reply reply) {
        if (reply.getContent().isBlank()) {
            return Response.failure(CommonErr.PARAM_WRONG.setMsg("内容不可为空!"));
        }
        reply.setUid(uid);
        reply.setContentAsText();
        MAPPER.reply.replyComment(reply);
        return Response.ok();
    }

    //回复另一个回复
    public Response giveNewReplyOnOther(int uid, Reply reply) {
        if (reply.getContent().isBlank()) {
            return Response.failure(CommonErr.PARAM_WRONG.setMsg("内容不可为空!"));
        }
        reply.setUid(uid);
        if (reply.checkSameCommentWithCallId()) {
            reply.addReplyPerson();
            MAPPER.reply.replyOtherReply(reply);
            return Response.ok();
        } else {
            return Response.failure(400,"不可跨评论回复!");
        }
    }

    //查看某一评论下回复
    public Response getAllReply(Integer commentId) {
        List<Map<String,Object>> replyList = MAPPER.reply.getAllReplyInComment(commentId);
        if (replyList.isEmpty()) {
            return Response.failure(CommonErr.NO_DATA);
        }

        int orderId = 1;
        Map<Object,Integer> idToOrderIdMap = new HashMap<>();
        idToOrderIdMap.put(-1,null);
        for (Map<String, Object> reply : replyList) {
            reply.put("content", HtmlHandleUtil.escapeToHTML(reply.get("content")));
            reply.put("avatarURL", FileUtil.getFileURL((String) reply.get("avatar"), FileType.IMAGE));
            idToOrderIdMap.put(reply.get("id"),orderId);
            reply.put("orderId",orderId++);
            //这里假定id靠前的order一定靠前，固可以这样做。
            reply.put("callOrderId",idToOrderIdMap.get(reply.get("callId")));
        }

        return Response.success(replyList);

    }


}
