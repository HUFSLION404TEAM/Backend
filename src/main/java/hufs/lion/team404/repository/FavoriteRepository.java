package hufs.lion.team404.repository;

import hufs.lion.team404.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    List<Favorite> findByUserId(Long userId);
    
    List<Favorite> findByUserIdAndFavoriteType(Long userId, Favorite.FavoriteType favoriteType);
    
    Optional<Favorite> findByUserIdAndTargetStudentId(Long userId, Long targetStudentId);
    
    Optional<Favorite> findByUserIdAndTargetProjectRequestId(Long userId, Long targetProjectRequestId);
    
    boolean existsByUserIdAndTargetStudentId(Long userId, Long targetStudentId);
    
    boolean existsByUserIdAndTargetProjectRequestId(Long userId, Long targetProjectRequestId);
    
    long countByTargetStudentId(Long targetStudentId);
    
    long countByTargetProjectRequestId(Long targetProjectRequestId);
}
