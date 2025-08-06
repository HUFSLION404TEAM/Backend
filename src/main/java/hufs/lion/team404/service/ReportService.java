package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.Report;
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
    
    public List<Report> findByReportedId(Long reportedId) {
        return reportRepository.findByReportedId(reportedId);
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
    
    public long countByReportedIdAndStatus(Long reportedId, Report.Status status) {
        return reportRepository.countByReportedIdAndStatus(reportedId, status);
    }
    
    public List<Report> findAll() {
        return reportRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        reportRepository.deleteById(id);
    }
}
