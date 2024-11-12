package student.forum.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.ENUM.FileType;
import student.forum.model.dto.JoinedSectionDTO;
import student.forum.model.po.Section;
import student.forum.model.po.User;
import student.forum.model.vo.CommonErr;
import student.forum.model.vo.Response;
import student.forum.util.CheckUtil;
import student.forum.util.FileUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SectionService {

    public Response createNewSection(User user, Section newSection, boolean beModerator) {
        if (MAPPER.section.judgeNameExists(newSection.getName())) {
            return Response.failure(400,"版块名称已经存在!");
        }

        if (!MAPPER.classify.judgeExistsById(newSection.getClassify())) {
            return Response.failure(400,"分类不存在!");
        }

        newSection.setModerator(beModerator ? user.getUid() : -1);

        MAPPER.section.create(newSection);

        if (beModerator) {
            int sectionId = MAPPER.section.getIdByName(newSection.getName());
            MAPPER.section_join.join(sectionId,user.getUid(),2);
        }
        return Response.ok();
    }

    public Response updateSectionIcon(User user, int id, MultipartFile image) {
        if (!(user.isSuperAdmin() || MAPPER.section.getModeratorBySection(id) == user.getUid())) {
            return Response.failure(CommonErr.NO_AUTHORITY);
        }

        String icon = FileUtil.uploadFile(image, FileType.IMAGE);
        MAPPER.section.updateIcon(id, icon);
        return Response.ok();
    }

    public Response updateSectionInformation(User user,
                                             int id,
                                             String slogan,
                                             Integer classify) {
        if (!(user.isSuperAdmin() || MAPPER.section.getModeratorBySection(id) == user.getUid())) {
            return Response.failure(CommonErr.NO_AUTHORITY);
        }

        String sql = "";
        boolean first = true;

        if (slogan != null && !slogan.isBlank()) {
            if (CheckUtil.checkSQL(slogan)) {
                return Response.failure(CommonErr.SQL_NOT_ALLOWED_IN_STRING);
            }
            sql += String.format("`slogan`='%s'",slogan);
            first = false;
        }

        if (classify != null) {
            if (!MAPPER.classify.judgeExistsById(classify)) {
                return Response.failure(400,"分类不存在!");
            }
            if (!first) sql += ",";
            else first = false;
            sql += String.format("`classify`='%d'",classify);
        }

        if (!first) {
            MAPPER.section.updateInformation(id,sql);
        }

        return Response.ok();
    }

    public Response setNewAdmin(User user, int sectionId, int uid) {
        Section section = MAPPER.section.getInformation(sectionId);
        if (!(user.isSuperAdmin() || Objects.equals(section.getModerator(), user.getUid()))) {
            return Response.failure(CommonErr.NO_AUTHORITY);
        }

        if (section.addAdmin(uid)) {
            MAPPER.section_join.updateIdentity(sectionId,uid,1);
            MAPPER.section.updateAdminList(sectionId,section.getAdmin());
            return Response.ok();
        }
        else return Response.failure(400,"添加失败");
    }

    public Response deleteAdmin(User user, int sectionId, int uid) {
        Section section = MAPPER.section.getInformation(sectionId);
        if (!(user.isSuperAdmin() || Objects.equals(section.getModerator(), user.getUid()))) {
            return Response.failure(CommonErr.NO_AUTHORITY);
        }

        if (section.deleteAdmin(uid)) {
            MAPPER.section_join.updateIdentity(sectionId,uid,0);
            MAPPER.section.updateAdminList(sectionId,section.getAdmin());
            return Response.ok();
        }
        else return Response.failure(400,"添加失败");
    }

    public Response getAllClassify() {
        return Response.success(MAPPER.classify.selectList(new QueryWrapper<>()));
    }

    public Response getAllSectionByClassify(Integer uid,Integer classifyId) {
        List<Map<String,Object>> sections = MAPPER.section.selectSectionsByClassifyId(uid,classifyId);
        if (sections.isEmpty()) {
            return Response.failure(CommonErr.NO_DATA);
        } else {
            for (Map<String,Object> i : sections) {
                i.put("iconURL",FileUtil.getFileURL(i.get("iconURL").toString(),FileType.IMAGE));
            }
        }
        return Response.success(sections);
    }

    public Response getMyJoinedSection(int uid) {
        List<JoinedSectionDTO> joinedSectionList = MAPPER.section.selectJoinedSectionByUid(uid);
        if (joinedSectionList.isEmpty()) {
            return Response.failure(CommonErr.NO_DATA);
        }
        else {
            return Response.success(joinedSectionList);
        }
    }

    public Response getSectionInfo(int uid,int id) {
        Section section = MAPPER.section.getInformation(id);
        if (section != null) {
            return Response.success(section.toReturnMap(uid));
        }
        else return Response.failure(CommonErr.NO_DATA);
    }

    public Response joinSection(int sectionId, int uid) {
        if (MAPPER.section_join.judgeExists(sectionId,uid)) {
            return Response.failure(CommonErr.OPERATE_REPEAT.setMsg("不可以重复关注哦"));
        }
        MAPPER.section_join.join(sectionId,uid);
        return Response.ok();
    }

    public Response cancelJoinSection(int sectionId, int uid) {
        if (MAPPER.section_join.judgeExists(sectionId,uid)) {
            MAPPER.section_join.cancel(sectionId, uid);
            return Response.ok();
        }
        return Response.failure(CommonErr.OPERATE_REPEAT.setMsg("不可以重复取关哦"));
    }

}
