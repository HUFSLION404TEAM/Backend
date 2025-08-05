package hufs.lion.team404.auth.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    private String profileImageUrl;



    public User update(String name, String profileImageUrl)
    {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        return this;
    }
}
