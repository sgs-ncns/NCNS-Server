package dev.ncns.sns.user.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_count")
@Entity
public class UserCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private Long postCount;

    @Column(nullable = false)
    private Long followerCount;

    @Column(nullable = false)
    private Long followingCount;

    @Column(nullable = false)
    private Long subscribingCount;

    @Builder
    public UserCount(Long userId) {
        this.userId = userId;
        this.postCount = 0L;
        this.followerCount = 0L;
        this.followingCount = 0L;
        this.subscribingCount = 0L;
    }

    public void increaseCount(CountType countType) {
        switch (countType) {
            case POST:
                this.postCount++;
                break;
            case FOLLOWER:
                this.followerCount++;
                break;
            case FOLLOWING:
                this.followingCount++;
                break;
            case SUBSCRIBING:
                this.subscribingCount++;
                break;
        }
    }

    public void decreaseCount(CountType countType) {
        switch (countType) {
            case POST:
                this.postCount--;
                break;
            case FOLLOWER:
                this.followerCount--;
                break;
            case FOLLOWING:
                this.followingCount--;
                break;
            case SUBSCRIBING:
                this.subscribingCount--;
                break;
        }
    }

}
