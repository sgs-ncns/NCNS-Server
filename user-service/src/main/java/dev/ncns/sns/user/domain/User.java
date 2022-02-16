package dev.ncns.sns.user.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update users set deleted_at=now() where id=?")
@Where(clause = "deleted_at is null")
@Table(name = "users")
@Entity
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String accountName;  // @accountName 계정명

    @Column(length = 50)
    private String nickname;    // 사용자 이름

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
    private AuthType authType;  // 회원가입 및 로그인 타입

    /** 유저의 마지막 활동 시간입니다. 휴면 계정 전환 등에 활용됩니다. */
    @Column
    private LocalDateTime accessAt;

    @Builder
    public User(String accountName, String nickname, String email, String password, Status status, AuthType authType) {
        this.accountName = accountName;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.status = status;
        this.authType = authType;
    }

    public void setPassword(String encode) {
        this.password = encode;
    }

    public void updateProfile(String accountName, String nickname, String introduce) {
        this.accountName = accountName;
        this.nickname = nickname;
        this.introduce = introduce;
    }

    public void updateAccessAt() {
        this.accessAt = LocalDateTime.now();
    }

}