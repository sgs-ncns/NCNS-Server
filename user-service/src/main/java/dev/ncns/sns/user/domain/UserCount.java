package dev.ncns.sns.user.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
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

    @Builder
    public UserCount(Long userId) {
        this.userId = userId;
        this.postCount = 0L;
        this.followerCount = 0L;
        this.followingCount = 0L;
    }

    public void update(CountType countType, boolean isUp) {
        if (isUp) {
            switch (countType) {
                case POST:
                    this.postCount++;
                    return;
                case FOLLOWER:
                    this.followerCount++;
                    return;
                case FOLLOWING:
                    this.followingCount++;
                    return;
            }
        } else {
            switch (countType) {
                case POST:
                    this.postCount--;
                    return;
                case FOLLOWER:
                    this.followerCount--;
                    return;
                case FOLLOWING:
                    this.followingCount--;
                    return;
            }
        }
    }
}
