package dev.ncns.sns.user.controller;

import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.user.common.SecurityUtil;
import dev.ncns.sns.user.dto.response.UserSummaryResponseDto;
import dev.ncns.sns.user.service.FollowService;
import dev.ncns.sns.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@ComponentScan(basePackages = "dev.ncns.sns.common.exception")
@RequestMapping(value = "/api/user")
@RestController
public class FollowController {

    @Value("${server.port}")
    private String port;

    private final UserService userService;
    private final FollowService followService;

    @Operation(summary = "팔로잉 목록 조회")
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowingList(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followingList = userService.getFollowingList(followService.getFollowingIdList(userId));
        return ResponseEntity.successResponse(port, "following list", followingList);
    }

    @Operation(summary = "팔로워 목록 조회")
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowerList(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followerList = userService.getFollowerList(followService.getFollowerIdList(userId));
        return ResponseEntity.successResponse(port, "follower list", followerList);
    }

    @Operation(summary = "팔로우/언팔로우 요청", description = "팔로우 중인 경우 -> 언팔로우 요청, 팔로우 중이 아닌 경우 -> 팔로우 요청")
    @PostMapping("/follow/{targetId}")
    public ResponseEntity<String> requestFollow(@PathVariable final Long targetId) {
        Long currentUserId = SecurityUtil.getCurrentMemberId();
        String data = followService.requestFollow(currentUserId, targetId);
        return ResponseEntity.successResponse(port, data);
    }

}
