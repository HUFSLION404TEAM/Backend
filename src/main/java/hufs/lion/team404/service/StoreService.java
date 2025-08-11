package hufs.lion.team404.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import hufs.lion.team404.domain.entity.Store;
import hufs.lion.team404.repository.StoreRepository;
import hufs.lion.team404.domain.dto.request.StoreUpdateRequestDto;
import hufs.lion.team404.domain.dto.request.StoreFilterRequest;
import hufs.lion.team404.domain.dto.response.StoreSummaryResponse;
import hufs.lion.team404.domain.dto.response.StoreDetailResponse;
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
    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }
    public Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("해당 가게를 찾을 수 없습니다.: id=" + storeId));
    }
    @Transactional
    public Store updateStore(Long storeId, StoreUpdateRequestDto dto, Long userId) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("Store not found: id=" + storeId));

        if (!store.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("해당 업체를 수정할 권한이 없습니다.");
        }

        if (dto.getStoreName() != null) store.setStoreName(dto.getStoreName());
        if (dto.getAddress()   != null) store.setAddress(dto.getAddress());
        if (dto.getContact()   != null) store.setContact(dto.getContact());
        if (dto.getCategory()  != null) store.setCategory(dto.getCategory());
        if (dto.getIntroduction()!= null) store.setIntroduction(dto.getIntroduction());

        return store;
    }
    @Transactional
    public void deleteStore(Long storeId, Long userId) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("Store not found: id=" + storeId));
        // 소유자 검증
        if (!store.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("해당 업체를 삭제할 권한이 없습니다.");
        }
        storeRepository.delete(store);
    }
    public Page<StoreSummaryResponse> listAll(Pageable pageable) {
        return storeRepository.findAll(pageable)
            .map(StoreSummaryResponse::from);
    }


    public Page<StoreSummaryResponse> listWithFilter(StoreFilterRequest f, Pageable pageable) {
        return storeRepository.search(
            f.keyword(), f.category(), f.address(), pageable
        ).map(StoreSummaryResponse::from);
    }


    public StoreDetailResponse getDetail(Long id) {
        Store s = storeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 가게를 찾을 수 없습니다.: id=" + id));
        return StoreDetailResponse.from(s);
    }
    public boolean hasStoreForUser(Long userId) {
        // 이미 findByUserId가 있으니 그대로 활용
        return storeRepository.findByUserId(userId).isPresent();
        // (원하면 레포지토리에 existsByUserId(Long) 추가 후 그걸 호출해도 됨)
    }

}

