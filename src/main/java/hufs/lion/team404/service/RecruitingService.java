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

	/**
	 * 필터링을 지원하는 구인글 조회
	 */
	public List<Recruiting> findAllWithFilters(String category, String keyword, Boolean isRecruiting) {
		List<Recruiting> recruitings = recruitingRepository.findAll();
		
		return recruitings.stream()
			.filter(recruiting -> {
				// 카테고리 필터
				if (category != null && !category.isEmpty() && recruiting.getStore() != null) {
					if (!category.equalsIgnoreCase(recruiting.getStore().getCategory())) {
						return false;
					}
				}
				
				// 키워드 필터 (제목에서 검색)
				if (keyword != null && !keyword.isEmpty()) {
					if (!recruiting.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
						return false;
					}
				}
				
				// 모집중 필터 (RecruitingListResponse의 로직 사용)
				if (isRecruiting != null) {
					boolean currentlyRecruiting = isCurrentlyRecruiting(recruiting.getRecruitmentPeriod());
					if (isRecruiting != currentlyRecruiting) {
						return false;
					}
				}
				
				return true;
			})
			.collect(java.util.stream.Collectors.toList());
	}

	/**
	 * 모집 기간을 파싱하여 현재 모집중인지 확인
	 */
	private boolean isCurrentlyRecruiting(String recruitmentPeriod) {
		try {
			if (recruitmentPeriod == null || !recruitmentPeriod.contains("~")) {
				return true;
			}
			
			String[] dates = recruitmentPeriod.split("~");
			if (dates.length != 2) {
				return true;
			}
			
			String endDateStr = dates[1].trim();
			java.time.LocalDate endDate = java.time.LocalDate.parse(endDateStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			java.time.LocalDate today = java.time.LocalDate.now();
			
			return !today.isAfter(endDate);
		} catch (Exception e) {
			return true;
		}
	}
	
	/**
	 * 특정 업체의 공고 목록 조회 (최신순)
	 */
	public List<Recruiting> findByStoreOrderByCreatedAtDesc(hufs.lion.team404.domain.entity.Store store) {
		return recruitingRepository.findByStoreOrderByCreatedAtDesc(store);
	}

	/**
	 * 구인글 삭제
	 */
	public void deleteById(Long id) {
		recruitingRepository.deleteById(id);
	}
}
