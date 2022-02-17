package dev.ncns.sns.user.dto.response;

import dev.ncns.sns.user.domain.User;
import dev.ncns.sns.user.domain.UserCount;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long userId;
    private final String accountName;
    private final String nickname;
    private final String introduce;

    private final Long postCount;
    private final Long followerCount;
    private final Long followingCount;
    private Long subscribingCount;

    private Boolean followStatus;
    private Boolean subscribeStatus;

    @Builder
    public UserResponseDto(User user, UserCount userCount) {
        this.userId = user.getId();
        this.accountName = user.getAccountName();
        this.nickname = user.getNickname();
        this.introduce = user.getIntroduce();
        this.postCount = userCount.getPostCount();
        this.followerCount = userCount.getFollowerCount();
        this.followingCount = userCount.getFollowingCount();
        this.subscribingCount = userCount.getSubscribingCount();
        this.followStatus = null;
        this.subscribeStatus = null;
    }

    public static UserResponseDto of(User user, UserCount userCount) {
        return UserResponseDto.builder().user(user).userCount(userCount).build();
    }

    public void setStatus(boolean followStatus, boolean subscribeStatus) {
        this.subscribingCount = null;
        this.followStatus = followStatus;
        this.subscribeStatus = subscribeStatus;
    }

}
