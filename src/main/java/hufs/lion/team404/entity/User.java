package hufs.lion.team404.entity;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

	// 연관관계
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Student student;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Store store;

	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
	private List<ChatMessage> sentMessages;

	@OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL)
	private List<Review> writtenReviews;

	@OneToMany(mappedBy = "reviewee", cascade = CascadeType.ALL)
	private List<Review> receivedReviews;

	@OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL)
	private List<Report> reportsMade;

	@OneToMany(mappedBy = "reported", cascade = CascadeType.ALL)
	private List<Report> reportsReceived;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Notification> notifications;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Favorite> favorites;

	@OneToMany(mappedBy = "payer", cascade = CascadeType.ALL)
	private List<Payment> payments;

	@OneToMany(mappedBy = "payee", cascade = CascadeType.ALL)
	private List<Payment> receivedPayments;
}
