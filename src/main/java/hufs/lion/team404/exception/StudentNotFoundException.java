package hufs.lion.team404.exception;

import hufs.lion.team404.domain.enums.ErrorCode;

/**
 * 학생을 찾을 수 없을 때 발생하는 예외
 */
public class StudentNotFoundException extends CustomException {
    
    public StudentNotFoundException() {
        super(ErrorCode.STUDENT_NOT_FOUND);
    }
    
    public StudentNotFoundException(String message) {
        super(ErrorCode.STUDENT_NOT_FOUND, message);
    }
    
    public StudentNotFoundException(Long studentId) {
        super(ErrorCode.STUDENT_NOT_FOUND, "학생 ID " + studentId + "를 찾을 수 없습니다.");
    }
}
