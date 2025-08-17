package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.Report;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    
    private final ReportRepository reportRepository;
    
    @Transactional
    public Report save(Report report) {
        return reportRepository.save(report);
    }
    
    public Optional<Report> findById(Long id) {
        return reportRepository.findById(id);
    }
    
    public List<Report> findByMatchingId(Long matchingId) {
        return reportRepository.findByMatchingId(matchingId);
    }
    
    public List<Report> findByReporterId(Long reporterId) {
        return reportRepository.findByReporterId(reporterId);
    }
    
    public List<Report> findByStatus(Report.Status status) {
        return reportRepository.findByStatus(status);
    }
    
    public List<Report> findByReportType(Report.ReportType reportType) {
        return reportRepository.findByReportType(reportType);
    }
    
    public List<Report> findByStatusOrderByCreatedAtDesc(Report.Status status) {
        return reportRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    public List<Report> findByReporterOrderByCreatedAtDesc(User reporter) {
        return reportRepository.findByReporterOrderByCreatedAtDesc(reporter);
    }
    
    public List<Report> findAllOrderByCreatedAtDesc() {
        return reportRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public boolean existsByMatchingAndReporter(Matching matching, User reporter) {
        return reportRepository.existsByMatchingAndReporter(matching, reporter);
    }
    
    public long countByStatus(Report.Status status) {
        return reportRepository.countByStatus(status);
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        reportRepository.deleteById(id);
    }
}
