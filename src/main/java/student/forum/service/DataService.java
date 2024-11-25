package student.forum.service;

import org.springframework.stereotype.Service;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.bo.SinglePageSearchBO;
import student.forum.model.po.User;
import student.forum.model.vo.CommonErr;
import student.forum.model.vo.Response;
import student.forum.model.vo.SinglePageVO;
import student.forum.util.TextUtil;

import java.util.*;

@Service
public class DataService {

    public Response getPopularActive(Integer top) {
        List<Map<String,Object>> topPostList = MAPPER.post.getTopPosts(top);
        if (topPostList.isEmpty()) {
            return Response.failure(CommonErr.NO_DATA);
        }
        return Response.success(topPostList);
    }

    public Response getMyObtainedLike(User user, SinglePageSearchBO<Map<String,Object>, Date[]> page) {

        List<Map<String,Object>> resultList = page.doSearch(new SinglePageSearchBO.Method<>() {
            @Override
            public List<Map<String, Object>> firstSearch(Integer pageSize) {
                List<Map<String, Object>> likeInComment = MAPPER.comment_like.getMyObtainedLikesInFirstTime(user.getUid(), pageSize);
                for (Map<String, Object> i : likeInComment) i.put("type", "comment");

                List<Map<String, Object>> likeInPost = MAPPER.post_like.getMyObtainedLikesInFirstTime(user.getUid(), pageSize);
                for (Map<String, Object> i : likeInPost) i.put("type", "post");

                return mergeListAndSetLastData(likeInComment, likeInPost, pageSize);
            }

            @Override
            public List<Map<String, Object>> searchTowardBack(Date[] startPos, Integer pageSize) {
                List<Map<String, Object>> likeInComment = startPos[0] == null ?
                        MAPPER.comment_like.getMyObtainedLikesInFirstTime(user.getUid(), pageSize) :
                        MAPPER.comment_like.getMyObtainedLikes(user.getUid(), pageSize, startPos[0]);
                for (Map<String, Object> i : likeInComment) i.put("type", "comment");

                List<Map<String, Object>> likeInPost = startPos[1] == null ?
                        MAPPER.post_like.getMyObtainedLikesInFirstTime(user.getUid(), pageSize) :
                        MAPPER.post_like.getMyObtainedLikes(user.getUid(), pageSize, startPos[1]);
                for (Map<String, Object> i : likeInPost) i.put("type", "post");

                return mergeListAndSetLastData(likeInComment, likeInPost, pageSize);
            }

            private List<Map<String,Object>> mergeListAndSetLastData(
                    List<Map<String,Object>> likeInComment,
                    List<Map<String,Object>> likeInPost,
                    Integer pageSize) {
                Date[] lastData = page.getLastData();
                if (lastData == null) {
                    lastData = new Date[2];
                    page.setLastData(lastData);
                }

                List<Map<String,Object>> mergedList = new ArrayList<>(likeInComment);
                mergedList.addAll(likeInPost);

                mergedList.sort((item1, item2) -> ((Date) item2.get("likeTime")).compareTo((Date) item1.get("likeTime")));
                if (mergedList.size() > pageSize) {
                    mergedList = mergedList.subList(0,pageSize);
                }

                boolean commentLastDataHasUpdate = false, postLastDataHasUpdate = false;
                for (int i = mergedList.size()-1; i >= 0 && !(commentLastDataHasUpdate && postLastDataHasUpdate); i--) {
                    if (!commentLastDataHasUpdate && mergedList.get(i).get("type").equals("comment")) {
                        lastData[0] = (Date) mergedList.get(i).get("likeTime");
                        commentLastDataHasUpdate = true;
                    }
                    if (!postLastDataHasUpdate && mergedList.get(i).get("type").equals("post")) {
                        lastData[1] = (Date) mergedList.get(i).get("likeTime");
                        postLastDataHasUpdate = true;
                    }
                }

                return mergedList;
            }

            @Override
            public Date[] updateLastData(List<Map<String,Object>> resultList) {
                return page.getLastData();
            }

        });

        for (Map<String,Object> i : resultList) {
            i.put("content", TextUtil.truncatedWords(11, (String) i.get("content")));
        }

        return Response.success(new SinglePageVO<>(page));
    }

    public Response getMyMessage(Integer uid, SinglePageSearchBO<Map<String,Object>, Date[]> page) {

        List<Map<String,Object>> resultList = page.doSearch(new SinglePageSearchBO.Method<>() {
            @Override
            public List<Map<String, Object>> firstSearch(Integer pageSize) {
                List<Map<String,Object>> commentList = MAPPER.comment.getCommentByUidOnFirstTime(uid, pageSize);
                for (Map<String, Object> i : commentList) i.put("type", "comment_post");

                List<Map<String,Object>> reply1List = MAPPER.reply.getReplyCommentByUidOnFirstTime(uid, pageSize);
                for (Map<String, Object> i : reply1List) i.put("type", "reply_comment");

                List<Map<String,Object>> reply2List = MAPPER.reply.getReplyReplyByUidOnFirstTime(uid, pageSize);
                for (Map<String, Object> i : reply2List) i.put("type", "reply_reply");

                return mergeListAndSetLastData(commentList,reply1List,reply2List,pageSize);
            }

            @Override
            public List<Map<String, Object>> searchTowardBack(Date[] startPos, Integer pageSize) {
                List<Map<String, Object>> commentList = startPos[0] == null ?
                        MAPPER.comment.getCommentByUidOnFirstTime(uid, pageSize) :
                        MAPPER.comment.getCommentByUid(uid, pageSize, startPos[0]);
                for (Map<String, Object> i : commentList) i.put("type", "comment_post");

                List<Map<String, Object>> reply1List = startPos[1] == null ?
                        MAPPER.reply.getReplyCommentByUidOnFirstTime(uid, pageSize) :
                        MAPPER.reply.getReplyCommentByUid(uid, pageSize, startPos[1]);
                for (Map<String, Object> i : reply1List) i.put("type", "reply_comment");

                List<Map<String, Object>> reply2List = startPos[1] == null ?
                        MAPPER.reply.getReplyReplyByUidOnFirstTime(uid, pageSize) :
                        MAPPER.reply.getReplyReplyByUid(uid, pageSize, startPos[1]);
                for (Map<String, Object> i : reply2List) i.put("type", "reply_comment");

                return mergeListAndSetLastData(commentList, reply1List, reply2List, pageSize);
            }

            private List<Map<String,Object>> mergeListAndSetLastData(
                    List<Map<String,Object>> commentList,
                    List<Map<String,Object>> reply1List,
                    List<Map<String,Object>> reply2List,
                    Integer pageSize) {
                Date[] lastData = page.getLastData();
                if (lastData == null) {
                    lastData = new Date[3];
                    page.setLastData(lastData);
                }

                List<Map<String,Object>> mergedList = new ArrayList<>(commentList);
                mergedList.addAll(reply1List);
                mergedList.addAll(reply2List);

                mergedList.sort((item1, item2) -> ((Date) item2.get("releaseTime")).compareTo((Date) item1.get("releaseTime")));
                if (mergedList.size() > pageSize) {
                    mergedList = mergedList.subList(0,pageSize);
                }

                boolean commentLastDataHasUpdate = false;
                boolean replyCommentLastDataHasUpdate = false;
                boolean replyReplyLastDataHasUpdate = false;
                for (int i = mergedList.size()-1; i >= 0; i--) {
                    if (!commentLastDataHasUpdate && mergedList.get(i).get("type").equals("comment_post")) {
                        lastData[0] = (Date) mergedList.get(i).get("releaseTime");
                        commentLastDataHasUpdate = true;
                    }
                    else if (!replyCommentLastDataHasUpdate && mergedList.get(i).get("type").equals("reply_comment")) {
                        lastData[1] = (Date) mergedList.get(i).get("releaseTime");
                        replyCommentLastDataHasUpdate = true;
                    }
                    else if (!replyReplyLastDataHasUpdate && mergedList.get(i).get("type").equals("reply_reply")) {
                        lastData[2] = (Date) mergedList.get(i).get("releaseTime");
                        replyReplyLastDataHasUpdate = true;
                    }
                    if (commentLastDataHasUpdate && replyCommentLastDataHasUpdate && replyReplyLastDataHasUpdate) {
                        break;
                    }
                }

                return mergedList;
            }

            @Override
            public Date[] updateLastData(List<Map<String,Object>> resultList) {
                return page.getLastData();
            }
        });

        for (Map<String,Object> i : resultList) {
            i.put("myContent", TextUtil.truncatedWords(11, (String) i.get("myContent")));
            i.put("replyContent", TextUtil.truncatedWords(50, (String) i.get("replyContent")));
        }

        return Response.success(new SinglePageVO<>(page));
    }

}
