package hufs.lion.team404.exception;

import hufs.lion.team404.domain.enums.ErrorCode;

/**
 * 업체를 찾을 수 없을 때 발생하는 예외
 */
public class StoreNotFoundException extends CustomException {
    
    public StoreNotFoundException() {
        super(ErrorCode.STORE_NOT_FOUND);
    }
    
    public StoreNotFoundException(String message) {
        super(ErrorCode.STORE_NOT_FOUND, message);
    }
    
    public StoreNotFoundException(Long storeId) {
        super(ErrorCode.STORE_NOT_FOUND, "업체 ID " + storeId + "를 찾을 수 없습니다.");
    }
}
