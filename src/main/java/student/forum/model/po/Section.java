package student.forum.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.CONSTANT.STATIC;
import student.forum.model.ENUM.FileType;
import student.forum.util.ArrayUtil;
import student.forum.util.FileUtil;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@TableName("section")
public class Section {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String icon;

    private String slogan;

    private Integer memberNum;

    private Integer postNum;

    private Integer classify;

    private Integer moderator = -1;

    private String admin;

    private Date createTime;

    public Map<String,Object> toReturnMap(int uid) {
        HashMap<String,Object> returnMap = new HashMap<>();
        returnMap.put("sectionId",id);
        returnMap.put("name",name);
        returnMap.put("iconURL",getIconURL());
        returnMap.put("slogan",slogan);
        returnMap.put("memberNum",memberNum);
        returnMap.put("postNum",postNum);
        returnMap.put("classifyId",classify);
        returnMap.put("classify",MAPPER.classify.selectById(classify).getName());
        returnMap.put("adminList",getAdminList());
        returnMap.put("createTime",createTime);
        SectionJoin sectionJoin = uid != -1 ? MAPPER.section_join.getInfo(id,uid) : null;
        if (sectionJoin != null) {
            returnMap.put("hasJoin",true);
            returnMap.put("joinTime",sectionJoin.getJoinTime());
            returnMap.put("identity",sectionJoin.getIdentity());
        } else {
            returnMap.put("hasJoin",false);
        }
        return returnMap;
    }

    public List<Map<String,Object>> getAdminList() {
        List<Map<String, Object>> adminList = new ArrayList<>();
        if (admin.length() > 2) {
            adminList.addAll(MAPPER.user.selectUserSimpleInfoListByUidList(ArrayUtil.JsonStringToString(admin)));
            for (Map<String, Object> i : adminList) {
                i.put("avatarURL", FileUtil.getFileURL(i.get("avatarURL").toString(), FileType.IMAGE));
                i.put("identity",1);
            }
        }
        if (moderator != -1) {
            Map<String, Object> moderatorInfo = MAPPER.user.selectUserSimpleInfoByUid(moderator);
            if (moderator != null) {
                moderatorInfo.put("avatarURL", FileUtil.getFileURL(moderatorInfo.get("avatarURL").toString(), FileType.IMAGE));
                moderatorInfo.put("identity", 2);
                adminList.add(0,moderatorInfo);
            }
        }
        return adminList;
    }

    @SneakyThrows
    public boolean addAdmin(int uid) {
        @SuppressWarnings("unchecked")
        List<Integer> adminList = STATIC.objectMapper.readValue(admin, List.class);
        if (adminList.size() > 3 || adminList.contains(uid)) return false;
        adminList.add(uid);
        admin = STATIC.objectMapper.writeValueAsString(adminList);
        return true;
    }

    @SneakyThrows
    public boolean deleteAdmin(int uid) {
        @SuppressWarnings("unchecked")
        List<Integer> adminList = STATIC.objectMapper.readValue(admin, List.class);
        for (int i = 0; i < adminList.size(); i++) {
            if (adminList.get(i) == uid) {
                adminList.remove(i);
                admin = STATIC.objectMapper.writeValueAsString(adminList);
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    public String getIconURL() {
        return FileUtil.getFileURL(icon, FileType.IMAGE);
    }

}
