package hufs.lion.team404.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private Boolean isAuthenticated = false;

	private String introduction;

	private String career;

	private String school;

	private String birth;

	private String phoneCall;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	// 연관관계
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<Portfolio> portfolios;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<AiMatchingSummary> aiMatchingSummaries;

	@Builder
	public Student(User user, String introduction, Boolean isAuthenticated, String career, String birth,
		String school,
		String phoneCall) {
		this.user = user;
		this.introduction = introduction;
		this.isAuthenticated = isAuthenticated;
		this.career = career;
		this.birth = birth;
		this.school = school;
		this.phoneCall = phoneCall;
	}
}
