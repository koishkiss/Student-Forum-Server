package student.forum.core.exception;

import student.forum.model.vo.CommonErr;

public class CommonErrException extends RuntimeException{
    public CommonErr ERROR;
    public CommonErrException(CommonErr err) {
        ERROR = err;
    }
}
