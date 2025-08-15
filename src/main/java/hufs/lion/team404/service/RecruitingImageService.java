package hufs.lion.team404.service;

import org.springframework.stereotype.Service;

import hufs.lion.team404.domain.entity.RecruitingImage;
import hufs.lion.team404.repository.RecruitingImageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitingImageService {
	private final RecruitingImageRepository recruitingImageRepository;

	public void save(RecruitingImage recruitingImage) {
		recruitingImageRepository.save(recruitingImage);
	}
}
