package dev.ncns.sns.user.controller;

import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.user.domain.ListType;
import dev.ncns.sns.user.domain.SubscribeStatus;
import dev.ncns.sns.user.dto.request.UpdateListRequestDto;
import dev.ncns.sns.user.dto.response.StatusResponseDto;
import dev.ncns.sns.user.dto.response.UserSummaryResponseDto;
import dev.ncns.sns.user.service.FollowService;
import dev.ncns.sns.user.service.SubscribeService;
import dev.ncns.sns.user.service.kafka.UserProducerService;
import dev.ncns.sns.user.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@RestController
public class FollowController extends ApiController {

    private final FollowService followService;
    private final SubscribeService subscribeService;
    private final UserProducerService kafkaService;

    @Operation(summary = "팔로잉 목록 조회")
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowingList(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followingList = followService.getFollowingList(userId);
        return getSuccessResponse("following list", followingList);
    }

    @Operation(summary = "팔로워 목록 조회")
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowerList(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followerList = followService.getFollowerList(userId);
        return getSuccessResponse("follower list", followerList);
    }

    @Operation(summary = "팔로우/언팔로우 요청", description = "팔로우 중인 경우 -> 언팔로우 요청 (구독 중인 경우 -> 구독취소 요청), 팔로우 중이 아닌 경우 -> 팔로우 요청")
    @PostMapping("/follow/{targetId}")
    public ResponseEntity<StatusResponseDto> requestFollow(@PathVariable final Long targetId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        StatusResponseDto statusResponse = followService.requestFollow(currentUserId, targetId);

        if (!statusResponse.getStatus() && !subscribeService.unsubscribeByUnfollow(currentUserId, targetId).getStatus()) {
            UpdateListRequestDto updateSubscribeListRequest = UpdateListRequestDto
                    .of(currentUserId, targetId, SubscribeStatus.UNSUBSCRIBE.getValue(), ListType.SUBSCRIBING);
            kafkaService.sendUpdateListRequest(updateSubscribeListRequest);
        }

        UpdateListRequestDto updateFollowListRequest = UpdateListRequestDto
                .of(currentUserId, targetId, statusResponse.getStatus(), ListType.FOLLOWING);
        kafkaService.sendUpdateListRequest(updateFollowListRequest);
        return getSuccessResponse(statusResponse);
    }

}
