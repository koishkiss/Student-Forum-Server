package student.forum.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import student.forum.model.po.Classify;
import student.forum.model.po.Section;
import student.forum.model.po.User;
import student.forum.model.vo.Response;
import student.forum.service.SectionService;

@RestController
@RequestMapping("/api")
public class SectionController {

    @Resource
    SectionService sectionService;

    //创建版块
    @PostMapping("/section/create")
    public Response createNewSection(
            HttpServletRequest request,
            @RequestBody Section section,
            @RequestParam(name = "beModerator",defaultValue = "true") boolean beModerator) {
        return sectionService.createNewSection((User) request.getAttribute("user"), section, beModerator);
    }

    //更改版块头像
    @PostMapping("/section/update/icon")
    public Response updateSectionIcon(
            HttpServletRequest request,
            @RequestParam(name = "sectionId") Integer id,
            @RequestParam(name = "image") MultipartFile image) {
        return sectionService.updateSectionIcon((User) request.getAttribute("user"), id, image);
    }

    //更改版块信息
    @PostMapping("/section/update/info")
    public Response updateSectionInfo(
            HttpServletRequest request,
            @RequestParam(name = "sectionId") Integer id,
            @RequestParam(name = "slogan", required = false) String slogan,
            @RequestParam(name = "classify", required = false) Integer classify) {
        return sectionService.updateSectionInformation((User) request.getAttribute("user"),id,slogan,classify);
    }

    //设置管理员
    @PostMapping("/section/member/addAdmin")
    public Response addAdmin(
            HttpServletRequest request,
            @RequestParam(name = "sectionId") Integer sectionId,
            @RequestParam(name = "uid") Integer uid) {
        return sectionService.setNewAdmin((User) request.getAttribute("user"),sectionId,uid);
    }

    //取消管理员
    @PostMapping("/section/member/deleteAdmin")
    public Response deleteAdmin(
            HttpServletRequest request,
            @RequestParam(name = "sectionId") Integer sectionId,
            @RequestParam(name = "uid") Integer uid) {
        return sectionService.deleteAdmin((User) request.getAttribute("user"),sectionId,uid);
    }

    //获取版块分类
    @GetMapping("/classify")
    public Response getAllClassify() {
        return sectionService.getAllClassify();
    }

    //新建版块分类
    @PostMapping("/classify/add")
    public Response addNewClassify(@RequestBody Classify classify) {
        return sectionService.addNewClassify(classify);
    }

    //根据分类获取版块
    @GetMapping("/section/classify")
    public Response getAllSectionByClassify(
            @RequestAttribute(name = "user") User user,
            @RequestParam(name = "classifyId") Integer classifyId) {
        return sectionService.getAllSectionByClassify(user.getUid(),classifyId);
    }

    //获取个人关注的版块
    @GetMapping("/section/mine")
    public Response getMyJoinedSection(
            @RequestAttribute(name = "user") User user) {
        return sectionService.getMyJoinedSection(user.getUid());
    }

    @GetMapping("/section/other")
    public Response getOtherJoinedSection(
            @RequestParam(name = "uid") Integer uid) {
        return sectionService.getMyJoinedSection(uid);
    }

    //查看版块信息
    @GetMapping("/section/info")
    public Response getSectionInfo(
            HttpServletRequest request,
            @RequestParam(name = "sectionId") Integer id) {
        Object o = request.getAttribute("user");
        if (o != null) {
            return sectionService.getSectionInfo(((User) o).getUid(), id);
        } else {
            return sectionService.getSectionInfo(-1,id);
        }
    }

    //关注该板块
    @GetMapping("/section/join")
    public Response joinSection(
            HttpServletRequest request,
            @RequestParam(name = "sectionId") Integer sectionId) {
        return sectionService.joinSection(sectionId,((User) request.getAttribute("user")).getUid());
    }

    //取消关注该板块
    @GetMapping("/section/cancelJoin")
    public Response cancelJoinSection(
            HttpServletRequest request,
            @RequestParam(name = "sectionId") Integer sectionId) {
        return sectionService.cancelJoinSection(sectionId, ((User) request.getAttribute("user")).getUid());
    }

}
