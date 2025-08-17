package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.Report;
import hufs.lion.team404.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    List<Report> findByMatchingId(Long matchingId);
    
    List<Report> findByReporterId(Long reporterId);

    List<Report> findByStatus(Report.Status status);
    
    List<Report> findByReportType(Report.ReportType reportType);
    
    List<Report> findByStatusOrderByCreatedAtDesc(Report.Status status);
    
    List<Report> findByReporterOrderByCreatedAtDesc(User reporter);
    
    List<Report> findAllByOrderByCreatedAtDesc();
    
    boolean existsByMatchingAndReporter(Matching matching, User reporter);
    
    long countByStatus(Report.Status status);
}
