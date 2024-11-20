package student.forum.service;

import org.springframework.stereotype.Service;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.bo.SinglePageSearchBO;
import student.forum.model.po.User;
import student.forum.model.vo.CommonErr;
import student.forum.model.vo.Response;
import student.forum.model.vo.SinglePageVO;

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

        page.doSearch(new SinglePageSearchBO.Method<>() {
            @Override
            public List<Map<String, Object>> firstSearch(Integer pageSize) {
                List<Map<String, Object>> likeInComment = MAPPER.comment_like.getMyObtainedLikes(user.getUid(), pageSize);
                for (Map<String, Object> i : likeInComment) i.put("type", "comment");

                List<Map<String, Object>> likeInPost = MAPPER.post_like.getMyObtainedLikes(user.getUid(), pageSize);
                for (Map<String, Object> i : likeInPost) i.put("type", "post");

                return mergeListAndSetLastData(likeInComment, likeInPost, pageSize);
            }

            @Override
            public List<Map<String, Object>> searchTowardBack(Date[] startPos, Integer pageSize) {
                List<Map<String, Object>> likeInComment = startPos[0] == null ?
                        MAPPER.comment_like.getMyObtainedLikes(user.getUid(), pageSize) :
                        MAPPER.comment_like.getMyObtainedLikes(user.getUid(), pageSize, startPos[0]);
                for (Map<String, Object> i : likeInComment) i.put("type", "comment");

                List<Map<String, Object>> likeInPost = startPos[1] == null ?
                        MAPPER.post_like.getMyObtainedLikes(user.getUid(), pageSize) :
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
                    mergedList = likeInPost.subList(0,pageSize);
                }

                boolean commentLastDataHasUpdate = false, postLastDataHasUpdate = false;
                for (int i = likeInPost.size()-1; i >= 0 && !(commentLastDataHasUpdate && postLastDataHasUpdate); i--) {
                    if (!commentLastDataHasUpdate && likeInPost.get(i).get("type").equals("comment")) {
                        lastData[0] = (Date) likeInPost.get(i).get("likeTime");
                        commentLastDataHasUpdate = true;
                    }
                    if (!postLastDataHasUpdate && likeInPost.get(i).get("type").equals("post")) {
                        lastData[1] = (Date) likeInPost.get(i).get("likeTime");
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

        return Response.success(new SinglePageVO<>(page));
    }

}
