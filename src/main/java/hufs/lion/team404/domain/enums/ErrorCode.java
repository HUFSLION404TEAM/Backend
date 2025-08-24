package hufs.lion.team404.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 정의
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_001", "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_002", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_003", "허용되지 않은 HTTP 메서드입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "COMMON_004", "잘못된 타입의 값입니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "COMMON_005", "접근이 거부되었습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_006", "잘못된 요청입니다."),

    // 인증/인가 에러
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "인증이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH_002", "권한이 없습니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_003", "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_004", "만료된 JWT 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_005", "유효하지 않은 Refresh 토큰입니다."),

    // 사용자 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_002", "이미 존재하는 사용자입니다."),
    INVALID_USER_TYPE(HttpStatus.BAD_REQUEST, "USER_003", "잘못된 사용자 타입입니다."),

    // 학생 에러
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDENT_001", "학생을 찾을 수 없습니다."),
    STUDENT_USER_ALREADY_HAVE(HttpStatus.BAD_REQUEST, "STUDENT_002", "유저에 이미 학생이 등록되었습니다."),
    STUDENT_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "STUDENT_003", "인증되지 않은 학생입니다."),

    // 업체 에러
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_001", "업체를 찾을 수 없습니다."),
    STORE_USER_ALREADY_HAVE(HttpStatus.BAD_REQUEST, "STORE_002", "유저에 이미 업체가 등록되었습니다."),
    STORE_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "STORE_003", "인증되지 않은 업체입니다."),

    // 매칭 에러
    MATCHING_NOT_FOUND(HttpStatus.NOT_FOUND, "MATCHING_001", "매칭을 찾을 수 없습니다."),
    MATCHING_ALREADY_EXISTS(HttpStatus.CONFLICT, "MATCHING_002", "이미 매칭이 존재합니다."),
    INVALID_MATCHING_STATUS(HttpStatus.BAD_REQUEST, "MATCHING_003", "잘못된 매칭 상태입니다."),

    // 프로젝트 에러
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_001", "프로젝트를 찾을 수 없습니다."),
    PROJECT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "PROJECT_002", "프로젝트에 접근할 권한이 없습니다."),

    // 채팅 에러
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_001", "채팅방을 찾을 수 없습니다."),
    CHAT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CHAT_002", "채팅방에 접근할 권한이 없습니다."),

    // 결제 에러
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_001", "결제 정보를 찾을 수 없습니다."),
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "PAYMENT_002", "결제에 실패했습니다."),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "PAYMENT_003", "잔액이 부족합니다."),

    // 파일 에러
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "FILE_001", "파일 업로드에 실패했습니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "FILE_002", "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "FILE_003", "파일 크기가 초과되었습니다."),

    // 공고 에러
    IMAGE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "IMAGE_001", "이미지 갯수가 초과되었습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "IMAGE_002", "파일이 비어있습니다."),

    // 리뷰 에러
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_001", "리뷰를 찾을 수 없습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "REVIEW_002", "이미 리뷰를 작성했습니다."),
    REVIEW_ACCESS_DENIED(HttpStatus.FORBIDDEN, "REVIEW_003", "리뷰에 접근할 권한이 없습니다."),

    // 신고 에러
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT_001", "신고를 찾을 수 없습니다."),
    REPORT_ALREADY_EXISTS(HttpStatus.CONFLICT, "REPORT_002", "이미 같은 내용으로 신고했습니다."),
    REPORT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "REPORT_003", "신고에 접근할 권한이 없습니다."),

    // 포트폴리오 에러
    PORTFOLIO_NOT_FOUND(HttpStatus.NOT_FOUND, "PORTFOLIO_001", "포트폴리오를 찾을 수 없습니다."),
    PORTFOLIO_ACCESS_DENIED(HttpStatus.FORBIDDEN, "PORTFOLIO_002", "포트폴리오에 접근할 권한이 없습니다."),

    // 지원서 에러
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLICATION_001", "지원서를 찾을 수 없습니다."),
    APPLICATION_ACCESS_DENIED(HttpStatus.FORBIDDEN, "APPLICATION_002", "지원서에 접근할 권한이 없습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
