package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findBySocialProviderAndSocialId(String socialProvider, String socialId);
    
    boolean existsByEmail(String email);
}
