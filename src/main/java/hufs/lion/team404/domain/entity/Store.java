package hufs.lion.team404.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stores")
@Data
@NoArgsConstructor
public class Store {

    @Id
    @Column(name = "business_number", nullable = false, unique = true)
    private String businessNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String storeName;

    private String address;

    private String contact;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    private Double temperature = 36.5; // 기본 온도 36.5도

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 연관관계
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProjectRequest> projectRequests;



    @Builder
    public Store(String businessNumber, User user, String storeName, String address, String contact, String category,
        String introduction, Double temperature) {
        this.businessNumber = businessNumber;
        this.user = user;
        this.storeName = storeName;
        this.address = address;
        this.contact = contact;
        this.category = category;
        this.introduction = introduction;
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
