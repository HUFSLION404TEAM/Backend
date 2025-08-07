package hufs.lion.team404.exception;

import hufs.lion.team404.domain.enums.ErrorCode;
import lombok.Getter;

/**
 * 커스텀 예외 클래스
 */
@Getter
public class CustomException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
