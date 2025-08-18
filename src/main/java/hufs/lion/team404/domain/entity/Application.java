package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Application {

    public enum Status {
        ACTIVE, CLOSED, MATCHED
    }

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	@Builder.Default
	private Status status = Status.ACTIVE;

	private String title;

	@Column(name = "project_name")
	private String projectName;

	@Column(name = "outline", columnDefinition = "TEXT")
	private String outline;

	@Column(name = "resolve_problem", columnDefinition = "TEXT")
	private String resolveProblem;

	@Column(name = "necessity", columnDefinition = "TEXT")
	private String necessity;

	@Column(name = "job_description", columnDefinition = "TEXT")
	private String jobDescription;

	@Column(name = "result", columnDefinition = "TEXT")
	private String result;

	@Column(name = "requirements", columnDefinition = "TEXT")
	private String requirements;

	@Column(name = "expected_outcome", columnDefinition = "TEXT")
	private String expectedOutcome;

	private LocalDateTime submittedAt;
	private LocalDateTime deletedAt;

	@CreatedDate @Column(updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	// 첨부파일과의 연관관계
	@OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ApplicationFile> files;

	@OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
	private List<Matching> matchings;

	@PrePersist
	void prePersist() {
		if (status == null) status = Status.ACTIVE;
	}
}