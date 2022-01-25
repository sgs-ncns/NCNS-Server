package dev.ncns.sns.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "users")
@SQLDelete(sql = "update users set deleted_at=now() where id=?")
@Where(clause = "deleted_at is null")
public class Users extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String account;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(length = 150)
    private String introduce;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AuthType authType;

    @Column
    private LocalDateTime accessAt;


    @Builder
    public Users(String account, String nickname, String email, String password, Status status, AuthType authType) {
        this.account = account;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.status = status;
        this.authType = authType;
    }

    public void setPassword(String encode) {
        this.password = encode;
    }

    public Users updateProfile(String account, String nickname, String introduce) {
        this.account = account;
        this.nickname = nickname;
        this.introduce = introduce;
        return this;
    }
}
