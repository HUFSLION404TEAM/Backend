package hufs.lion.team404.repository;

import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.Review;
import hufs.lion.team404.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByMatching(Matching matching);
    
    boolean existsByMatchingAndReviewer(Matching matching, User reviewer);
}
