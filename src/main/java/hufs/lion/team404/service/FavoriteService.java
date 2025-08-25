package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.Favorite;
import hufs.lion.team404.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    
    private final FavoriteRepository favoriteRepository;
    
    @Transactional
    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }
    
    public Optional<Favorite> findById(Long id) {
        return favoriteRepository.findById(id);
    }
    
    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }
    
    public List<Favorite> findByUserIdAndFavoriteType(Long userId, Favorite.FavoriteType favoriteType) {
        return favoriteRepository.findByUserIdAndFavoriteType(userId, favoriteType);
    }
    
    // 새로운 메서드들 (학생 유저와 구인글용)
    public Optional<Favorite> findByUserIdAndTargetStudentUserId(Long userId, Long targetStudentUserId) {
        return favoriteRepository.findByUserIdAndTargetStudentUserId(userId, targetStudentUserId);
    }
    
    public Optional<Favorite> findByUserIdAndTargetRecruitingId(Long userId, Long targetRecruitingId) {
        return favoriteRepository.findByUserIdAndTargetRecruitingId(userId, targetRecruitingId);
    }
    
    public boolean existsByUserIdAndTargetStudentUserId(Long userId, Long targetStudentUserId) {
        return favoriteRepository.existsByUserIdAndTargetStudentUserId(userId, targetStudentUserId);
    }
    
    public boolean existsByUserIdAndTargetRecruitingId(Long userId, Long targetRecruitingId) {
        return favoriteRepository.existsByUserIdAndTargetRecruitingId(userId, targetRecruitingId);
    }
    
    public long countByTargetStudentUserId(Long targetStudentUserId) {
        return favoriteRepository.countByTargetStudentUserId(targetStudentUserId);
    }
    
    public long countByTargetRecruitingId(Long targetRecruitingId) {
        return favoriteRepository.countByTargetRecruitingId(targetRecruitingId);
    }
    
    public List<Favorite> findAll() {
        return favoriteRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        favoriteRepository.deleteById(id);
    }
}
