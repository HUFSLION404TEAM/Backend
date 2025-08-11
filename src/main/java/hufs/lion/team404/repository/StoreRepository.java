package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByUserId(Long userId);
    
    Optional<Store> findByBusinessNumber(String businessNumber);
    
    List<Store> findByCategory(String category);
    
    List<Store> findByStoreNameContaining(String storeName);
    boolean existsByUserId(Long userId);
    boolean existsByBusinessNumber(String businessNumber);
    @Query("""
select s from Store s
 where (:keyword is null
        or lower(s.storeName)    like lower(concat('%', :keyword, '%'))
        or lower(s.introduction) like lower(concat('%', :keyword, '%')))
   and (:category is null or s.category = :category)
   and (:address  is null or lower(s.address) like lower(concat('%', :address, '%')))
""")
    Page<Store> search(@Param("keyword") String keyword,
        @Param("category") String category,
        @Param("address")  String address,
        Pageable pageable);

}
