package dev.ncns.sns.user;

import dev.ncns.sns.user.service.FollowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FollowServiceTest extends UserApplicationTests {
    @Autowired
    private FollowService followService;

    @Test
    public void requestFollow() {
        followService.requestFollow(1L, 2L);
        followService.requestFollow(1L, 3L);
        followService.requestFollow(3L, 1L);
        followService.requestFollow(2L, 3L);
    }

    @Test
    public void getFollowingList() {
        Long userId = 1L;

        List<Long> followingIdList = followService.getFollowingIdList(userId);
        for (Long id : followingIdList) {
            System.out.println(id);
        }
    }

    @Test
    public void getFollowerList() {
        Long userId = 1L;

        List<Long> followerIdList = followService.getFollowerIdList(userId);
        for (Long id : followerIdList) {
            System.out.println(id);
        }
    }
}
