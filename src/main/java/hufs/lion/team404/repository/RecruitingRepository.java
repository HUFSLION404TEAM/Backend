package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import hufs.lion.team404.domain.entity.Recruiting;

import java.util.List;

public interface RecruitingRepository extends JpaRepository<Recruiting, Long> {
    
    /**
     * 업체별 공고 목록 조회 (최신순)
     */
    List<Recruiting> findByStoreOrderByCreatedAtDesc(Store store);
}
