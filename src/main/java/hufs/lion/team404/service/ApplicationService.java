package hufs.lion.team404.service;

import hufs.lion.team404.domain.dto.request.ApplicationSaveRequestDto;
import hufs.lion.team404.domain.entity.Application;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {
    
    private final ApplicationRepository applicationRepository;
    private final UserService userService;
    
    @Transactional
    public Application save(Application application) {
        return applicationRepository.save(application);
    }
    
    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }
    
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }
}
