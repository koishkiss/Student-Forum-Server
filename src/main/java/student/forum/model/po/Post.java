package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.ENUM.FileType;
import student.forum.util.FileUtil;
import student.forum.util.HtmlHandleUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@TableName("post")
public class Post {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer sectionId;

    private Integer uid;

    private String title;

    private String cover;

    private String content;

    private Date postTime;

    private Integer viewNum;

    private Integer likeNum;

    private Integer bookmarkNum;

    private Integer commentNum;

    private Integer status;

    public Map<String,Object> toReturnMap(int viewerUid) {
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("postId",id);
        returnMap.put("sectionId",sectionId);
        returnMap.put("uid",uid);
        User user = MAPPER.user.selectUserByUid(uid);
        int sectionModerator = MAPPER.section.getModeratorBySection(sectionId);
        returnMap.put("avatarURL", user.getAvatarURL());
        returnMap.put("nickname", user.getNickname());
        returnMap.put("isModerator", uid == sectionModerator);
        returnMap.put("title",title);
        if (cover != null) {
            returnMap.put("coverURL", FileUtil.getFileURL(cover, FileType.IMAGE));
        }
        //将text转义为html
        if (content != null) {
            setContentAsHTML();
        }
        returnMap.put("content",content);
        returnMap.put("postTime",postTime);
        returnMap.put("viewNum",viewNum);
        returnMap.put("likeNum",likeNum);
        returnMap.put("bookmarkNum",bookmarkNum);
        returnMap.put("commentNum",commentNum);
        returnMap.put("status",status);
        returnMap.put("isLiked", MAPPER.post_like.judgeLiked(viewerUid,id));
        returnMap.put("isMarked",MAPPER.post_mark.judgeMarked(viewerUid,id));
        return returnMap;
    }

    public void setContentAsText() {
        content = HtmlHandleUtil.escapeFromHTML(content);
    }

    public void setContentAsHTML() {
        content = HtmlHandleUtil.escapeToHTML(content);
    }

    public void addViews(int viewerUid) {
        if (MAPPER.post_view.judgeViewed(viewerUid,id)) return;
        viewNum++;
        MAPPER.post_view.viewed(viewerUid,id);
    }
}
