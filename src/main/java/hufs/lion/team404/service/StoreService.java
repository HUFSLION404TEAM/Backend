package hufs.lion.team404.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.repository.StoreRepository;
import hufs.lion.team404.domain.dto.request.StoreUpdateRequestDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    public Optional<Store> findById(String businessNumber) {
        return storeRepository.findById(businessNumber);
    }

    public List<Store> findByUser(User user) {
        return storeRepository.findByUser(user);
    }

    public List<Store> findByUserId(Long userId) {
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
    public void deleteById(String businessNumber) {
        storeRepository.deleteById(businessNumber);
    }

    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    public Store getStoreById(String businessNumber) {
        return storeRepository.findById(businessNumber)
            .orElseThrow(() -> new NotFoundException("해당 가게를 찾을 수 없습니다.: businessNumber=" + businessNumber));
    }

    @Transactional
    public Store updateStore(String businessNumber, StoreUpdateRequestDto dto, Long userId) {
        Store store = storeRepository.findById(businessNumber)
            .orElseThrow(() -> new NotFoundException("Store not found: businessNumber=" + businessNumber));

        if (!store.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("해당 업체를 수정할 권한이 없습니다.");
        }

        if (dto.getStoreName() != null) store.setStoreName(dto.getStoreName());
        if (dto.getAddress() != null) store.setAddress(dto.getAddress());
        if (dto.getContact() != null) store.setContact(dto.getContact());
        if (dto.getCategory() != null) store.setCategory(dto.getCategory());
        if (dto.getIntroduction() != null) store.setIntroduction(dto.getIntroduction());

        return store;
    }

    @Transactional
    public void deleteStore(String businessNumber, Long userId) {
        Store store = storeRepository.findById(businessNumber)
            .orElseThrow(() -> new NotFoundException("Store not found: businessNumber=" + businessNumber));
        // 소유자 검증
        if (!store.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("해당 업체를 삭제할 권한이 없습니다.");
        }
        storeRepository.delete(store); // 물리 삭제
    }
}