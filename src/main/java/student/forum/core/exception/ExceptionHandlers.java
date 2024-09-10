package student.forum.core.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import student.forum.model.vo.CommonErr;
import student.forum.model.vo.Response;

/*
* 进行一个错误的全局捕获
* */

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(RuntimeException.class)
    public Response error(Exception e) {
        if (e instanceof MyBatisSystemException) {
            System.out.println("连接到数据库异常!");
            e.printStackTrace();
            return Response.failure(CommonErr.CONNECT_TO_MYSQL_FAILED);
        }
        if (e instanceof MaxUploadSizeExceededException) {
            System.out.println(e.getMessage());
            return Response.failure(CommonErr.FILE_OUT_OF_LIMIT);
        }

        if (e instanceof CommonErrException) {
            System.out.println(((CommonErrException) e).ERROR.getMessage());
            return Response.failure(((CommonErrException) e).ERROR);
        }
        if (e instanceof TokenException) {
            System.out.println(e.getMessage());
            return Response.failure(CommonErr.TOKEN_CHECK_FAILED.setMsg(e.getMessage()));
        }
        if (e instanceof HttpMessageNotReadableException) {
            if (e.getCause() instanceof JsonMappingException && e.getCause().getCause() instanceof  CheckException) {
                System.out.println(e.getCause().getMessage());
                return Response.failure(CommonErr.POST_CHECK_FAILED.setMsg(e.getCause().getCause().getMessage()));
            }
        }

        System.out.println(e.getMessage());
        e.printStackTrace();
        return Response.error(401, String.valueOf(e));
    }

}
