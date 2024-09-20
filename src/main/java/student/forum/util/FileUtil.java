package student.forum.util;

/*
* 上传文件工具类
* */

import org.springframework.web.multipart.MultipartFile;
import student.forum.core.exception.CommonErrException;
import student.forum.model.CONSTANT.VALUE;
import student.forum.model.ENUM.FileType;
import student.forum.model.vo.CommonErr;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileUtil {



    //根据原文件名生成唯一不重复文件名
    private static String generateNewFileName(String originalFileName) {
        return UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    //上传文件，返回文件名称
    public static String uploadFile(MultipartFile file, FileType fileType) {
        //判断文件不为null
        if (file == null) throw new CommonErrException(CommonErr.FILE_FORMAT_ERROR.setMsg("不可上传空文件!"));
        //限制文件大小
        if (file.getSize() > fileType.max_size) throw new CommonErrException(CommonErr.FILE_FORMAT_ERROR.setMsg("文件过大!"));
        //获取原文件名
        String originalImgName = file.getOriginalFilename();

        //判断原文件格式正确性
        if (originalImgName != null && Arrays.stream(fileType.suffix).anyMatch(originalImgName::endsWith)) {
            //生成新文件名(随机uuid+原文件后缀)
            String newFileName = generateNewFileName(originalImgName);
            //生成新文件路径
            String photoLocalPath = fileType.local_path + newFileName;
            File newFilePath = new File(photoLocalPath);

            //判断文件父目录是否存在
            if (!newFilePath.getParentFile().exists() && !newFilePath.getParentFile().mkdirs()) {
                throw new RuntimeException("服务器好像开小差了，请再试试!");
            }

            //保存文件
            try {
                file.transferTo(newFilePath);
            } catch (IllegalStateException | IOException e) {
                throw new RuntimeException("服务器好像开小差了，请再试试!");
            }

            //返回文件名称
            return newFileName;
        }
        else throw new CommonErrException(CommonErr.FILE_FORMAT_ERROR.setMsg("文件格式错误!"));
    }

    public static String getFileURL(String fileName, FileType fileType) {
        return VALUE.web_path + fileType.web_path + fileName;
    }

}
