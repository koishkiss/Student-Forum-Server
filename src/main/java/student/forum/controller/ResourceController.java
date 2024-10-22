package student.forum.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import student.forum.model.ENUM.FileType;
import student.forum.model.vo.Response;
import student.forum.service.ResourceService;

@RestController
public class ResourceController {

    @Resource
    ResourceService resourceService;

    @PostMapping("/upload/photo")
    Response uploadPhoto(@RequestParam(name = "photo") MultipartFile file) {
        return resourceService.updateFile(file, FileType.IMAGE);
    }
    
}
