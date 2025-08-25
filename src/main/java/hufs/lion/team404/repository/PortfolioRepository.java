package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.domain.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    /**
     * 학생별 포트폴리오 목록 조회 (최신순)
     */
    List<Portfolio> findByStudentOrderByCreatedAtDesc(Student student);
    
    /**
     * 공개 포트폴리오 목록 조회 (최신순)
     */
    List<Portfolio> findByIsPrivateFalseOrderByCreatedAtDesc();
    
    /**
     * 특정 학생의 공개 포트폴리오 목록 조회 (최신순)
     */
    List<Portfolio> findByStudentAndIsPrivateFalseOrderByCreatedAtDesc(Student student);
    
    /**
     * 특정 학생의 공개 포트폴리오 수 조회
     */
    Long countByStudentAndIsPrivateFalse(Student student);
}
