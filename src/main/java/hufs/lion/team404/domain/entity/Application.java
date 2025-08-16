package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(
	name = "applications",
	indexes = {
		@Index(name = "idx_app_student_store_status", columnList = "student_id,store_id,status")
	}
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Application {

	public enum Status { DRAFT, SUBMITTED, DELETED }

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long studentId;

	@Column(nullable = false)
	private Long storeId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Status status = Status.DRAFT;


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
}


