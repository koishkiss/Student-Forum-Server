package student.forum.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import student.forum.model.po.Section;
import student.forum.model.po.User;
import student.forum.model.vo.Response;
import student.forum.service.SectionService;

@RestController
public class SectionController {

    @Resource
    SectionService sectionService;

    //创建版块
    @PostMapping("/section/create")
    public Response createNewSection(
            HttpServletRequest request,
            @RequestBody Section section,
            @RequestParam(name = "beModerator") boolean beModerator) {
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
    @GetMapping("/section/update/info")
    public Response updateSectionInfo(
            HttpServletRequest request,
            @RequestParam(name = "sectionId") Integer id,
            @RequestParam(name = "slogan") String slogan,
            @RequestParam(name = "classify") Integer classify) {
        return sectionService.updateSectionInformation((User) request.getAttribute("user"),id,slogan,classify);
    }

    //查看版块信息
    @GetMapping("/section/info")
    public Response getSectionInfo(@RequestParam(name = "sectionId") Integer id) {
        return sectionService.getSectionInfo(id);
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
