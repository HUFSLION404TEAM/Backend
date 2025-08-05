package hufs.lion.team404.repository;

import hufs.lion.team404.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByUserId(Long userId);
    
    List<Student> findByIsAuthenticated(Boolean isAuthenticated);
    
    List<Student> findBySchool(String school);
}
