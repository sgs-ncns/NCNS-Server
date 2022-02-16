package dev.ncns.sns.user.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_count")
@Entity
public class UserCount {

    /**
     * count 전용 테이블입니다.
     * profile 조회 시 사용되는 작성글 수, 팔로워 수, 팔로잉 수 가 포함돼있습니다.
     * count 시 select 쿼리를 줄이기 위해 모델링 되었습니다.
     */
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
