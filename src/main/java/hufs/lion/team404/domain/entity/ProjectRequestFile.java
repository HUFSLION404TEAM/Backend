package hufs.lion.team404.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProjectRequestFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filePath;

    private String originalFileName; // 원본 파일명
    private String savedFileName;    // 저장된 파일명 (UUID)
    private Long fileSize;           // 파일 크기 (bytes)
    private String contentType;      // MIME 타입

    @Column(nullable = false)
    private Integer fileOrder;

    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_request_id", nullable = false)
    @JsonIgnore
    private ProjectRequest projectRequest;

    @Builder
    public ProjectRequestFile(String filePath, String originalFileName, String savedFileName, Long fileSize, String contentType,
                              Integer fileOrder, LocalDateTime uploadedAt, ProjectRequest projectRequest) {

        this.filePath = filePath;
        this.originalFileName = originalFileName;
        this.savedFileName = savedFileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.fileOrder = fileOrder;
        this.uploadedAt = uploadedAt;
        this.projectRequest = projectRequest;
    }
}
