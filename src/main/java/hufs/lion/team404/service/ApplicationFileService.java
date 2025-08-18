package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.Application;
import hufs.lion.team404.domain.entity.ApplicationFile;
import hufs.lion.team404.repository.ApplicationFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationFileService {

    private final ApplicationFileRepository applicationFileRepository;

    public void save(ApplicationFile applicationFile) { 
        applicationFileRepository.save(applicationFile);
    }

    public void delete(ApplicationFile applicationFile) { 
        applicationFileRepository.delete(applicationFile);
    }

    @Transactional
    public void update(Application application, List<MultipartFile> files) {
        if (files == null) return;

        var existing = applicationFileRepository
                .findByApplicationIdOrderByFileOrder(application.getId());
        applicationFileRepository.deleteAll(existing);

        if (files.isEmpty()) return;

        int order = 1;
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null) {
                int dot = originalName.lastIndexOf('.');
                if (dot != -1) {
                    ext = originalName.substring(dot);
                }
            }
            String saved = java.util.UUID.randomUUID().toString() + ext;

            ApplicationFile applicationFile = ApplicationFile.builder()
                    .application(application)
                    .filePath("/uploads/application/" + saved)
                    .originalFileName(originalName)
                    .savedFileName(saved)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .fileOrder(order++)
                    .uploadedAt(java.time.LocalDateTime.now())
                    .build();

            applicationFileRepository.save(applicationFile);
        }
    }

    @Transactional
    public void deleteAllByApplicationId(Long applicationId) {
        var existing = applicationFileRepository
                .findByApplicationIdOrderByFileOrder(applicationId);
        if (!existing.isEmpty()) {
            applicationFileRepository.deleteAll(existing);
        }
    }
}
