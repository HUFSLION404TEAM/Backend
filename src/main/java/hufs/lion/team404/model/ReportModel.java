package hufs.lion.team404.model;

import hufs.lion.team404.domain.dto.request.ReportCreateRequestDto;
import hufs.lion.team404.domain.dto.request.ReportHandleRequestDto;
import hufs.lion.team404.domain.dto.response.ReportResponse;
import hufs.lion.team404.domain.entity.*;
import hufs.lion.team404.domain.enums.ErrorCode;
import hufs.lion.team404.domain.enums.UserRole;
import hufs.lion.team404.exception.CustomException;
import hufs.lion.team404.service.MatchingService;
import hufs.lion.team404.service.ReportService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportModel {
    
    private final ReportService reportService;
    private final UserService userService;
    private final MatchingService matchingService;
    
    /**
     * 신고 작성
     */
    @Transactional
    public Long createReport(ReportCreateRequestDto request, String email) {
        log.info("Creating report for matching: {}, reporter email: {}", 
                request.getMatchingId(), email);
        
        User reporter = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        Matching matching = matchingService.findById(request.getMatchingId())
                .orElseThrow(() -> new CustomException(ErrorCode.MATCHING_NOT_FOUND));
        
        // 매칭 참여자인지 확인
        validateMatchingParticipant(matching, reporter);
        
        // 이미 같은 매칭에 대해 신고했는지 확인
        boolean alreadyReported = reportService.existsByMatchingAndReporter(matching, reporter);
        if (alreadyReported) {
            throw new CustomException(ErrorCode.REPORT_ALREADY_EXISTS);
        }
        
        // 신고 생성
        Report report = new Report();
        report.setMatching(matching);
        report.setReporter(reporter);
        report.setReportType(request.getReportType());
        report.setDescription(request.getDescription());
        report.setStatus(Report.Status.PENDING);
        
        // 신고자 타입 설정
        if (reporter.getStudent() != null) {
            report.setReporterType(Report.ReporterType.STUDENT);
        } else if (reporter.getStore() != null) {
            report.setReporterType(Report.ReporterType.STORE);
        }
        
        Report savedReport = reportService.save(report);
        
        log.info("Report created successfully. ID: {}, Type: {}", savedReport.getId(), request.getReportType());
        return savedReport.getId();
    }
    
    /**
     * 매칭 참여자 확인
     */
    private void validateMatchingParticipant(Matching matching, User user) {
        User student = matching.getChatRoom().getStudent().getUser();
        User store = matching.getChatRoom().getStore().getUser();
        
        if (!user.getId().equals(student.getId()) && !user.getId().equals(store.getId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
    
    /**
     * 신고자별 신고 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ReportResponse> getReportsByReporter(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        List<Report> reports = reportService.findByReporterOrderByCreatedAtDesc(user);
        return reports.stream()
                .map(ReportResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 모든 신고 조회 (관리자용)
     */
    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReports(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 관리자 권한 확인
        if (user.getUserRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        
        List<Report> reports = reportService.findAllOrderByCreatedAtDesc();
        return reports.stream()
                .map(ReportResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 처리 대기 중인 신고 조회 (관리자용)
     */
    @Transactional(readOnly = true)
    public List<ReportResponse> getPendingReports(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 관리자 권한 확인
        if (user.getUserRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        
        List<Report> reports = reportService.findByStatusOrderByCreatedAtDesc(Report.Status.PENDING);
        return reports.stream()
                .map(ReportResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 신고 처리 (관리자용)
     */
    @Transactional
    public Long handleReport(Long reportId, ReportHandleRequestDto request, String email) {
        log.info("Handling report: {}, admin email: {}", reportId, email);
        
        User admin = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 관리자 권한 확인
        if (admin.getUserRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        
        Report report = reportService.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
        
        // 신고 처리
        report.setStatus(request.getStatus());
        report.setAdminResponse(request.getAdminResponse());
        report.setHandledBy(admin.getId());
        report.setResolvedAt(LocalDateTime.now());
        
        Report savedReport = reportService.save(report);
        
        log.info("Report {} handled successfully by admin: {}", reportId, admin.getId());
        return savedReport.getId();
    }
    
    /**
     * 신고 상세 조회
     */
    @Transactional(readOnly = true)
    public ReportResponse getReport(Long reportId, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        Report report = reportService.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
        
        // 접근 권한 확인 (신고자, 피신고자, 관리자만 조회 가능)
        boolean canView = false;
        if (user.getUserRole() == UserRole.ADMIN) {
            canView = true;
        } else if (report.getReporter().getId().equals(user.getId())) {
            canView = true;
        } else {
            User reported = report.getReported();
            if (reported != null && reported.getId().equals(user.getId())) {
                canView = true;
            }
        }
        
        if (!canView) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        
        return ReportResponse.fromEntity(report);
    }
}
