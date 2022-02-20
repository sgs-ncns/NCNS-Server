package dev.ncns.sns.user.controller;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.user.dto.request.*;
import dev.ncns.sns.user.dto.response.CheckResponseDto;
import dev.ncns.sns.user.dto.response.LoginResponseDto;
import dev.ncns.sns.user.dto.response.UserResponseDto;
import dev.ncns.sns.user.service.FollowService;
import dev.ncns.sns.user.service.SubscribeService;
import dev.ncns.sns.user.service.UserService;
import dev.ncns.sns.user.service.kafka.UserProducerService;
import dev.ncns.sns.user.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@RestController
public class UserController extends ApiController {

    private final UserService userService;
    private final FollowService followService;
    private final SubscribeService subscribeService;
    private final UserProducerService kafkaService;

    @Operation(summary = "회원가입", description = "auth type(APPLE|GOOGLE|LOCAL)")
    @NonAuthorize
    @PostMapping
    public ResponseEntity<Void> signUp(@Validated @RequestBody SignUpRequestDto signUpRequest) {
        Long userId = userService.signUp(signUpRequest);
        UserConsumerRequestDto userConsumerRequest = UserConsumerRequestDto.of(userId, signUpRequest.getAccountName(), signUpRequest.getNickname());
        kafkaService.sendCreateDocumentRequest(userConsumerRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "회원탈퇴")
    @DeleteMapping
    public ResponseEntity<Void> signOut() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        userService.checkExistUser(currentUserId);
        followService.deleteFollow(currentUserId);
        subscribeService.deleteSubscribe(currentUserId);
        // TODO: post 삭제
        userService.signOut(currentUserId);
        kafkaService.sendDeleteDocumentRequest(currentUserId);
        return getSuccessResponse();
    }

    @Operation(summary = "사용자 정보 수정")
    @PatchMapping
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileRequestDto updateProfileRequest) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        userService.updateProfile(currentUserId, updateProfileRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "사용자 프로필 조회")
    @GetMapping("/{accountName}")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable String accountName) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        UserResponseDto userResponse = userService.getProfile(accountName);
        if (!currentUserId.equals(userResponse.getUserId())) {
            boolean followStatus = followService.getFollowStatus(currentUserId, userResponse.getUserId());
            boolean subscribeStatus = subscribeService.getSubscribeStatus(currentUserId, userResponse.getUserId());
            userResponse.setStatus(followStatus, subscribeStatus);
        }
        return getSuccessResponse(userResponse);
    }

    @Operation(summary = "이메일 중복 체크", description = "중복인 경우 true, 중복이 아닌 경우 false")
    @NonAuthorize
    @PostMapping("/email")
    public ResponseEntity<CheckResponseDto> checkDuplicateEmail(@Valid @RequestBody CheckEmailRequestDto checkEmailRequest) {
        CheckResponseDto checkResponse = userService.isDuplicateEmail(checkEmailRequest);
        return getSuccessResponse(checkResponse);
    }

    @Operation(summary = "계정 이름 중복 체크", description = "중복인 경우 true, 중복이 아닌 경우 false")
    @NonAuthorize
    @PostMapping("/account")
    public ResponseEntity<CheckResponseDto> checkDuplicateAccountName(@Valid @RequestBody CheckAccountRequestDto checkAccountRequest) {
        CheckResponseDto checkResponse = userService.isDuplicateAccountName(checkAccountRequest);
        return getSuccessResponse(checkResponse);
    }

    @ApiIgnore
    @NonAuthorize
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto loginResponse = userService.handleLoginRequest(loginRequest);
        userService.updateUserAccessAt(loginResponse.getUserId());
        return getSuccessResponse(loginResponse);
    }

    @ApiIgnore
    @NonAuthorize
    @PostMapping("/count/post")
    public ResponseEntity<Void> updateUserPostCount(@RequestBody UpdateUserPostCountDto dto) {
        userService.updatePostCount(dto);
        return getSuccessResponse();
    }

    @ApiIgnore
    @NonAuthorize
    @PutMapping("/access")
    public ResponseEntity<Void> updateUserAccessAt(@RequestBody UpdateAccessAtRequestDto updateAccessAtRequest) {
        userService.updateUserAccessAt(updateAccessAtRequest.getUserId());
        return getSuccessResponse();
    }

}