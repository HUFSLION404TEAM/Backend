package hufs.lion.team404.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hufs.lion.team404.domain.entity.RecruitingImage;

import java.util.List;

public interface RecruitingImageRepository extends JpaRepository<RecruitingImage, Long> {

    List<RecruitingImage> findByRecruitingIdOrderByImageOrderAsc(Long recruitingId);
}
