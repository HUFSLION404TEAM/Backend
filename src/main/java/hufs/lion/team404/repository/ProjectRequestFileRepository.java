package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.ProjectRequestFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRequestFileRepository extends JpaRepository<ProjectRequestFile, Long> {
    List<ProjectRequestFile> findByProjectRequestId(Long projectRequestId);
    List<ProjectRequestFile> findByProjectRequestIdOrderByFileOrderAsc(Long projectRequestId);
}
