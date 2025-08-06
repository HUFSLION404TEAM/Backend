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
    
    public Optional<Favorite> findByUserIdAndTargetStudentId(Long userId, Long targetStudentId) {
        return favoriteRepository.findByUserIdAndTargetStudentId(userId, targetStudentId);
    }
    
    public Optional<Favorite> findByUserIdAndTargetProjectRequestId(Long userId, Long targetProjectRequestId) {
        return favoriteRepository.findByUserIdAndTargetProjectRequestId(userId, targetProjectRequestId);
    }
    
    public boolean existsByUserIdAndTargetStudentId(Long userId, Long targetStudentId) {
        return favoriteRepository.existsByUserIdAndTargetStudentId(userId, targetStudentId);
    }
    
    public boolean existsByUserIdAndTargetProjectRequestId(Long userId, Long targetProjectRequestId) {
        return favoriteRepository.existsByUserIdAndTargetProjectRequestId(userId, targetProjectRequestId);
    }
    
    public long countByTargetStudentId(Long targetStudentId) {
        return favoriteRepository.countByTargetStudentId(targetStudentId);
    }
    
    public long countByTargetProjectRequestId(Long targetProjectRequestId) {
        return favoriteRepository.countByTargetProjectRequestId(targetProjectRequestId);
    }
    
    public List<Favorite> findAll() {
        return favoriteRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        favoriteRepository.deleteById(id);
    }
}
