package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    List<Favorite> findByUserId(Long userId);
    
    List<Favorite> findByUserIdAndFavoriteType(Long userId, Favorite.FavoriteType favoriteType);
    
    // 새로운 메서드들 (학생 유저와 구인글용)
    Optional<Favorite> findByUserIdAndTargetStudentUserId(Long userId, Long targetStudentUserId);
    
    Optional<Favorite> findByUserIdAndTargetRecruitingId(Long userId, Long targetRecruitingId);
    
    boolean existsByUserIdAndTargetStudentUserId(Long userId, Long targetStudentUserId);
    
    boolean existsByUserIdAndTargetRecruitingId(Long userId, Long targetRecruitingId);
    
    long countByTargetStudentUserId(Long targetStudentUserId);
    
    long countByTargetRecruitingId(Long targetRecruitingId);
}
