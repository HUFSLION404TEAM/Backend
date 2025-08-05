package hufs.lion.team404.service;

import hufs.lion.team404.entity.User;
import hufs.lion.team404.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findBySocialProviderAndSocialId(String socialProvider, String socialId) {
        return userRepository.findBySocialProviderAndSocialId(socialProvider, socialId);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    @Transactional
    public User findOrCreateKakaoUser(String email, String socialId, String nickname) {
        return userRepository.findBySocialProviderAndSocialId("kakao", socialId)
            .orElseGet(() -> {
                User newUser = User.builder()
                    .email(email)
                    .socialProvider("kakao")
                    .socialId(socialId)
                    .nickname(nickname)
                    .role("USER") // 너희 프로젝트 기준에 따라 수정 가능
                    .build();
                return userRepository.save(newUser);
            });
    }
}




