package hufs.lion.team404.exception;

import hufs.lion.team404.domain.enums.ErrorCode;

/**
 * 사용자를 찾을 수 없을 때 발생하는 예외
 */
public class UserNotFoundException extends CustomException {
    
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
    
    public UserNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }
    
    public UserNotFoundException(Long userId) {
        super(ErrorCode.USER_NOT_FOUND, "사용자 ID " + userId + "를 찾을 수 없습니다.");
    }
}
