package hufs.lion.team404.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruiting")
@Data
@NoArgsConstructor
public class Recruiting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private List<String> imagesUrl;
	private String recruitmentPeriod;
	private String progressPeriod;

	private String price;

	private String projectOutline;
	private String expectedResults;
	private String detailRequirement;

	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;

	@OneToMany(mappedBy = "recruiting", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RecruitingImage> images = new ArrayList<>();

	@Builder
	public Recruiting(String title, String recruitmentPeriod, String progressPeriod, String projectOutline,
		String price,
		String expectedResults, Store store, String detailRequirement) {
		this.title = title;
		this.recruitmentPeriod = recruitmentPeriod;
		this.progressPeriod = progressPeriod;
		this.projectOutline = projectOutline;
		this.price = price;
		this.expectedResults = expectedResults;
		this.store = store;
		this.detailRequirement = detailRequirement;
	}
}
