package hufs.lion.team404.service;

import hufs.lion.team404.entity.Store;
import hufs.lion.team404.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    
    private final StoreRepository storeRepository;
    
    @Transactional
    public Store save(Store store) {
        return storeRepository.save(store);
    }
    
    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }
    
    public Optional<Store> findByUserId(Long userId) {
        return storeRepository.findByUserId(userId);
    }
    
    public Optional<Store> findByBusinessNumber(String businessNumber) {
        return storeRepository.findByBusinessNumber(businessNumber);
    }
    
    public List<Store> findByCategory(String category) {
        return storeRepository.findByCategory(category);
    }
    
    public List<Store> findByStoreNameContaining(String storeName) {
        return storeRepository.findByStoreNameContaining(storeName);
    }
    
    public boolean existsByBusinessNumber(String businessNumber) {
        return storeRepository.existsByBusinessNumber(businessNumber);
    }
    
    public List<Store> findAll() {
        return storeRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        storeRepository.deleteById(id);
    }
}
