package hufs.lion.team404.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PortfolioImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String imagePath;

	private String originalFileName; // 원본 파일명
	private String savedFileName;    // 저장된 파일명 (UUID)
	private Long fileSize;           // 파일 크기 (bytes)
	private String contentType;      // MIME 타입 (image/jpeg, image/png 등)

	@Column(nullable = false)
	private Integer imageOrder;

	private LocalDateTime uploadedAt; // 업로드 시간

	@ManyToOne
	@JoinColumn(name = "portfolio_id")
	private Portfolio portfolio;
}
