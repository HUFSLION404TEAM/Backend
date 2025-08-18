package hufs.lion.team404.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hufs.lion.team404.domain.entity.Recruiting;
import hufs.lion.team404.repository.RecruitingRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitingService {
	private final RecruitingRepository recruitingRepository;

	public void save(Recruiting recruiting) {
		recruitingRepository.save(recruiting);
	}

	public Optional<Recruiting> findById(Long id) {
		return recruitingRepository.findById(id);
	}

	public List<Recruiting> findAll() {
		return recruitingRepository.findAll();
	}
}
