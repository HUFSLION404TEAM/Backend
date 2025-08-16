package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.PortfolioImage;
import hufs.lion.team404.domain.entity.ProjectRequest;
import hufs.lion.team404.domain.entity.ProjectRequestFile;
import hufs.lion.team404.repository.ProjectRequestFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRequestFileService {

    private final ProjectRequestFileRepository projectRequestFileRepository;

    public void save(ProjectRequestFile projectRequestFile) { projectRequestFileRepository.save(projectRequestFile);}

    public void delete(ProjectRequestFile projectRequestFile) { projectRequestFileRepository.delete(projectRequestFile);}

    @Transactional
    public void update(ProjectRequest projectRequest, List<MultipartFile> files) {
        if (files == null) return;

        var existing = projectRequestFileRepository
                .findByProjectRequestIdOrderByFileOrderAsc(projectRequest.getId());
        projectRequestFileRepository.deleteAll(existing);

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

            ProjectRequestFile  projectRequestFile = ProjectRequestFile.builder()
                    .projectRequest(projectRequest)
                    .filePath("/uploads/project-request/" + saved)
                    .originalFileName(originalName)
                    .savedFileName(saved)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .fileOrder(order++)
                    .uploadedAt(java.time.LocalDateTime.now())
                    .build();

            projectRequestFileRepository.save(projectRequestFile);
        }
    }
    @Transactional
    public void deleteAllByProjectRequestId(Long projectRequestId) {
        var existing = projectRequestFileRepository
                .findByProjectRequestIdOrderByFileOrderAsc(projectRequestId);
        if (!existing.isEmpty()) {
            projectRequestFileRepository.deleteAll(existing);
        }
    }



}
