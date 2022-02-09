package dev.ncns.sns.user.dto.response;

import dev.ncns.sns.user.domain.UserCount;
import dev.ncns.sns.user.domain.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String accountName;
    private String nickname;
    private String introduce;
    private Long postCount;
    private Long followerCount;
    private Long followingCount;

    @Builder
    public UserResponseDto(Users user, UserCount userCount) {
        this.id = user.getId();
        this.accountName = user.getAccountName();
        this.nickname = user.getNickname();
        this.introduce = user.getIntroduce();
        this.postCount = userCount.getPostCount();
        this.followerCount = userCount.getFollowerCount();
        this.followingCount = userCount.getFollowingCount();
    }

    public static UserResponseDto of(Users user, UserCount userCount) {
        return UserResponseDto.builder().user(user).userCount(userCount).build();
    }

}