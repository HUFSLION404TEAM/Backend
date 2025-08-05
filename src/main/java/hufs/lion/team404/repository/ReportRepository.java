package hufs.lion.team404.repository;

import hufs.lion.team404.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    List<Report> findByMatchingId(Long matchingId);
    
    List<Report> findByReporterId(Long reporterId);
    
    List<Report> findByReportedId(Long reportedId);
    
    List<Report> findByStatus(Report.Status status);
    
    List<Report> findByReportType(Report.ReportType reportType);
    
    List<Report> findByStatusOrderByCreatedAtDesc(Report.Status status);
    
    long countByReportedIdAndStatus(Long reportedId, Report.Status status);
}
