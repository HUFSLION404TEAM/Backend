package hufs.lion.team404.service;

import org.springframework.stereotype.Service;

import hufs.lion.team404.domain.entity.Recruiting;
import hufs.lion.team404.repository.RecruitingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitingService {
	private final RecruitingRepository recruitingRepository;

	public void save(Recruiting recruiting) {
		recruitingRepository.save(recruiting);
	}
}
