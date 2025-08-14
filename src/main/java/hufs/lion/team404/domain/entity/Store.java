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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String storeName;

    @Column(unique = true, nullable = false)
    private String businessNumber;

    private String address;

    private String contact;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 연관관계
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProjectRequest> projectRequests;



    @Builder
    public Store(User user, String storeName, String businessNumber, String address, String contact, String category,
        String introduction) {
        this.user = user;
        this.storeName = storeName;
        this.businessNumber = businessNumber;
        this.address = address;
        this.contact = contact;
        this.category = category;
        this.introduction = introduction;
    }
}
