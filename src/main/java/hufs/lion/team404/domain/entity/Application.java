package hufs.lion.team404.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class Application {

	// ★ 별도 파일 없이 상태를 내부 enum으로 정의
	public enum Status { DRAFT, SUBMITTED, DELETED }

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)  // 지원자(학생)
	private Long studentId;

	@Column(nullable = false)  // 지원 대상 가게
	private Long storeId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Status status;

	private String title;
	private LocalDateTime submittedAt;
	private LocalDateTime deletedAt;

	@CreatedDate @Column(updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;
	@ElementCollection
	@CollectionTable(name = "application_answers", joinColumns = @JoinColumn(name = "application_id"))
	@MapKeyColumn(name = "question_key")
	@Column(name = "answer", length = 2000)
	private Map<String, String> answers = new HashMap<>();
}

