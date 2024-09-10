package student.forum.core.exception;

import lombok.Getter;

@Getter
public class CheckException extends RuntimeException {
    private final String message;
    public CheckException(String msg) {
        message = msg;
    }
}
