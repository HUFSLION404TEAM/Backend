package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.ApplicationFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationFileRepository extends JpaRepository<ApplicationFile, Long> {
    List<ApplicationFile> findByApplicationIdOrderByFileOrder(Long applicationId);
    void deleteByApplicationId(Long applicationId);
}
