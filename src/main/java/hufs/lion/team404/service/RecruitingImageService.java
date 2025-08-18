package hufs.lion.team404.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import hufs.lion.team404.domain.entity.RecruitingImage;
import hufs.lion.team404.repository.RecruitingImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitingImageService {
	private final RecruitingImageRepository recruitingImageRepository;

	public void save(RecruitingImage recruitingImage) {
		recruitingImageRepository.save(recruitingImage);
	}

	@Transactional(readOnly = true)
	public List<RecruitingImage> findByRecruitingId(Long recruitingId) {
		return recruitingImageRepository.findByRecruitingIdOrderByImageOrderAsc(recruitingId);
	}

}
