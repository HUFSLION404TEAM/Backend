package hufs.lion.team404.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hufs.lion.team404.domain.enums.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	private String profileImage;

	private String socialProvider;

	private String socialId;

	private String temperature;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	// 연관관계 - 순환 참조 방지를 위해 JsonIgnore 추가
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private Student student;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private Store store;

	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ChatMessage> sentMessages;

	@OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Review> writtenReviews;

	@OneToMany(mappedBy = "reviewee", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Review> receivedReviews;

	@OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Report> reportsMade;

	@OneToMany(mappedBy = "reported", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Report> reportsReceived;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Notification> notifications;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Favorite> favorites;

	@OneToMany(mappedBy = "payer", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Payment> payments;

	@OneToMany(mappedBy = "payee", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Payment> receivedPayments;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole userRole;

	@Builder
	public User(String name, String email, String profileImage, String socialProvider, String socialId,
		String temperature, LocalDateTime createdAt, LocalDateTime updatedAt, Student student, Store store,
		List<ChatMessage> sentMessages, List<Review> writtenReviews, List<Review> receivedReviews,
		List<Report> reportsMade, List<Report> reportsReceived, List<Notification> notifications,
		List<Favorite> favorites, List<Payment> payments, List<Payment> receivedPayments, UserRole userRole) {
		this.name = name;
		this.email = email;
		this.profileImage = profileImage;
		this.socialProvider = socialProvider;
		this.socialId = socialId;
		this.temperature = temperature;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.student = student;
		this.store = store;
		this.sentMessages = sentMessages;
		this.writtenReviews = writtenReviews;
		this.receivedReviews = receivedReviews;
		this.reportsMade = reportsMade;
		this.reportsReceived = reportsReceived;
		this.notifications = notifications;
		this.favorites = favorites;
		this.payments = payments;
		this.receivedPayments = receivedPayments;
		this.userRole = userRole;
	}

	public String getRoleKey() {
		return this.userRole.getKey();
	}

	public User update(String name, String picture) {
		this.name = name;
		this.profileImage = picture;

		return this;
	}
}
