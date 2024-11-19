package student.forum.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import student.forum.model.ENUM.FileType;
import student.forum.model.vo.Response;
import student.forum.util.FileUtil;

@Service
public class ResourceService {

    public Response updateFile(MultipartFile file, FileType fileType) {
        String icon = FileUtil.uploadFile(file, fileType);

        @Getter
        @AllArgsConstructor
        class returnDataVo {
            private String icon;
            @SuppressWarnings("unused")
            public String getIconURL() {
                return FileUtil.getFileURL(icon, FileType.IMAGE);
            }
        }

        return Response.success(new returnDataVo(icon));
    }

}
