package hufs.lion.team404.model;

import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.service.MatchingService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingModel {
    
    private final MatchingService matchingService;
    private final UserService userService;
    
    /**
     * 매칭 수락
     */
    @Transactional
    public Long acceptMatching(Long matchingId, String email) {
        log.info("Accepting matching: {}, user email: {}", matchingId, email);
        
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        Matching matching = matchingService.findById(matchingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MATCHING_NOT_FOUND));
        
        // 권한 확인
        validateMatchingPermission(matching, user);
        
        // 이미 응답한 매칭인지 확인
        if (!matching.isPending()) {
            throw new CustomException(ErrorCode.INVALID_MATCHING_STATUS);
        }
        
        matching.accept();
        Matching savedMatching = matchingService.save(matching);
        
        log.info("Matching {} accepted successfully", matchingId);
        return savedMatching.getId();
    }
    
    /**
     * 매칭 거절
     */
    @Transactional
    public Long rejectMatching(Long matchingId, String email) {
        log.info("Rejecting matching: {}, user email: {}", matchingId, email);
        
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        Matching matching = matchingService.findById(matchingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MATCHING_NOT_FOUND));
        
        // 권한 확인
        validateMatchingPermission(matching, user);
        
        // 이미 응답한 매칭인지 확인
        if (!matching.isPending()) {
            throw new CustomException(ErrorCode.INVALID_MATCHING_STATUS);
        }
        
        matching.reject();
        Matching savedMatching = matchingService.save(matching);
        
        log.info("Matching {} rejected successfully", matchingId);
        return savedMatching.getId();
    }
    
    /**
     * 매칭 권한 확인
     */
    private void validateMatchingPermission(Matching matching, User user) {
        boolean canRespond = false;
        
        if (matching.getMatchedBy() == Matching.MatchedBy.STORE_OFFER) {
            // 소상공인이 의뢰한 경우 -> 대학생이 수락/거절
            if (matching.getPortfolio() != null && 
                matching.getPortfolio().getStudent().getUser().getId().equals(user.getId())) {
                canRespond = true;
            }
        } else if (matching.getMatchedBy() == Matching.MatchedBy.STUDENT_APPLY) {
            // 대학생이 지원한 경우 -> 소상공인이 수락/거절
            if (matching.getProjectRequest().getStore().getUser().getId().equals(user.getId())) {
                canRespond = true;
            }
        }
        
        if (!canRespond) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
    
    /**
     * 매칭 조회
     */
    @Transactional(readOnly = true)
    public Matching getMatching(Long matchingId, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        Matching matching = matchingService.findById(matchingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MATCHING_NOT_FOUND));
        
        // 조회 권한 확인 (매칭 관련자만 조회 가능)
        boolean canView = false;
        if (matching.getPortfolio() != null && 
            matching.getPortfolio().getStudent().getUser().getId().equals(user.getId())) {
            canView = true;
        } else if (matching.getProjectRequest().getStore().getUser().getId().equals(user.getId())) {
            canView = true;
        }
        
        if (!canView) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        
        return matching;
    }
}
