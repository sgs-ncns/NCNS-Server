package dev.ncns.sns.user;

import dev.ncns.sns.user.dto.response.UserSummaryResponseDto;
import dev.ncns.sns.user.service.FollowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FollowServiceTest extends UserApplicationTests {
    @Autowired
    private FollowService followService;

    @Test
    public void requestFollow() {
        followService.requestFollow(2L, 1L);
        followService.requestFollow(2L, 3L);
        followService.requestFollow(1L, 3L);
    }

    @Test
    public void getFollowingList() {
        Long userId = 2L;

        List<UserSummaryResponseDto> followingIdList = followService.getFollowingList(userId);
        for (UserSummaryResponseDto user : followingIdList) {
            System.out.println(user.getId());
        }
    }

    @Test
    public void getFollowerList() {
        Long userId = 3L;

        List<UserSummaryResponseDto> followerIdList = followService.getFollowerList(userId);
        for (UserSummaryResponseDto user : followerIdList) {
            System.out.println(user.getId());
        }
    }
}
