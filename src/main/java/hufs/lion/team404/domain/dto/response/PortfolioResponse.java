package hufs.lion.team404.domain.dto.response;

import hufs.lion.team404.domain.entity.Matching;
import hufs.lion.team404.domain.entity.PortfolioImage;
import hufs.lion.team404.domain.entity.Student;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PortfolioResponse {
    private Long id;
    private Long studentId;
    private String title;
    private String progressPeriod;
    private boolean prize;
    private String workDoneProgress;
    private String result;
    private String felt;
    private boolean isPrivate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Matching> matchings;
    private List<PortfolioImage> images = new ArrayList<>();
}
