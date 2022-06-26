package kit.prolog.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "USERS")
@NoArgsConstructor
@Data
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String account;
    @Column(nullable = false)
    private String password;
    private Integer sns;
    private String email;
    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private Boolean alarm;      // false : 알람 미수신, true : 알람 수신
    private String image;
    private String nickname;
    private String introduce;

    @OneToMany(mappedBy = "user")
    private List<Like> likes = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    public User(Long id) {
        this.id = id;
    }
}
