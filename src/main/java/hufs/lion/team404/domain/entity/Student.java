package hufs.lion.team404.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	private String major; // 전공

	private String capabilities; // 보유 역량

	private String birth;

	private String phoneCall;

	private Boolean isPublic; // 학생 정보 조회 공개/비공개

	private Boolean isEmployment; // 구직 중 / 휴식 중

	private String region; // 지역

	private Double temperature = 36.5; // 기본 온도 36.5도

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	// 연관관계
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Portfolio> portfolios;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<AiMatchingSummary> aiMatchingSummaries;

	@Builder
	public Student(User user, String introduction, Boolean isAuthenticated, String career, String birth,
		String school, String major, String capabilities,
		String phoneCall, Boolean isPublic, Boolean isEmployment, String region, Double temperature) {
		this.user = user;
		this.introduction = introduction;
		this.isAuthenticated = isAuthenticated;
		this.career = career;
		this.birth = birth;
		this.school = school;
		this.major = major;
		this.capabilities = capabilities;
		this.phoneCall = phoneCall;
		this.isPublic = isPublic;
		this.isEmployment = isEmployment;
		this.region = region;
		this.temperature = temperature != null ? temperature : 36.5;
	}

	/**
	 * 리뷰 별점에 따른 온도 조정
	 * @param rating 리뷰 별점 (0~5)
	 */
	public void adjustTemperature(Integer rating) {
		if (rating == null) return;
		
		double adjustment = 0.0;
		
		if (rating >= 0 && rating < 0.5) {
			adjustment = -1.0; // 1도 하강
		} else if (rating >= 1 && rating < 1.5) {
			adjustment = -0.5; // 0.5도 하강
		} else if (rating >= 1.5 && rating < 2.5) {
			adjustment = -0.3; // 0.3도 하강
		} else if (rating >= 2.5 && rating < 3.5) {
			adjustment = 0.3; // 0.3도 상승
		} else if (rating >= 3.5 && rating < 4.5) {
			adjustment = 0.5; // 0.5도 상승
		} else if (rating >= 4.5) {
			adjustment = 1.0; // 1도 상승
		}
		
		this.temperature = Math.max(0.0, Math.min(99.9, this.temperature + adjustment));
	}
}
