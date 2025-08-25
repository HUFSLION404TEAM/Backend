package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    
    Optional<Student> findByUserId(Long userId);
    
    List<Student> findByIsAuthenticated(Boolean isAuthenticated);
    
    List<Student> findBySchool(String school);
    
    /**
     * 공개된 학생 프로필을 온도 높은 순, 최신 가입순으로 조회
     */
    List<Student> findByIsPublicTrueOrderByTemperatureDescCreatedAtDesc();
}
