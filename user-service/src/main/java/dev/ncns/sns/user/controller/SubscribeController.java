package dev.ncns.sns.user.controller;

import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.user.domain.ListType;
import dev.ncns.sns.user.dto.request.UpdateListRequestDto;
import dev.ncns.sns.user.dto.response.StatusResponseDto;
import dev.ncns.sns.user.dto.response.UserSummaryResponseDto;
import dev.ncns.sns.user.service.SubscribeService;
import dev.ncns.sns.user.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@RestController
public class SubscribeController extends ApiController {

    private final SubscribeService subscribeService;
    private final FeedFeignClient feedFeignClient;

    @Operation(summary = "구독(깐부) 목록 조회", description = "사용자 본인의 구독(깐부) 목록 조회 (본인 외에는 비공개)")
    @GetMapping("/subscribing")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowingList() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        List<UserSummaryResponseDto> subscribingList = subscribeService.getSubscribingList(currentUserId);
        return getSuccessResponse("subscribing list", subscribingList);
    }

    @Operation(summary = "구독/구독취소 요청", description = "구독 중인 경우 -> 구독취소 요청, 구독 중이 아닌 경우 -> 구독 요청")
    @PostMapping("/subscribe/{targetId}")
    public ResponseEntity<StatusResponseDto> requestSubscribe(@PathVariable Long targetId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        StatusResponseDto statusResponse = subscribeService.requestSubscribe(currentUserId, targetId);
        UpdateListRequestDto dto = UpdateListRequestDto
                .of(currentUserId, targetId, statusResponse.getStatus(), ListType.SUBSCRIBING);
        feedFeignClient.updateList(dto);
        return getSuccessResponse(statusResponse);
    }

}