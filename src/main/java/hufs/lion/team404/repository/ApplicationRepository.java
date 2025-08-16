package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {


	Optional<Application> findTopByStudentIdAndStoreIdAndStatusOrderByIdDesc(
		Long studentId, Long storeId, Application.Status status);
	Optional<Application> findByIdAndStudentId(Long id, Long studentId);

}
