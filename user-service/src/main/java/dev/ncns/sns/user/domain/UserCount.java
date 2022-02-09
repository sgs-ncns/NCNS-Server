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
