package hufs.lion.team404.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hufs.lion.team404.auth.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
