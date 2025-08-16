package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByUserId(Long userId);

    Optional<Store> findByBusinessNumber(String businessNumber);

    List<Store> findByCategory(String category);

    List<Store> findByStoreNameContaining(String storeName);

    boolean existsByBusinessNumber(String businessNumber);
}
