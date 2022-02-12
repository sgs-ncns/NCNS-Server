package dev.ncns.sns.user.controller;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.user.common.SecurityUtil;
import dev.ncns.sns.user.dto.request.*;
import dev.ncns.sns.user.dto.response.CheckResponseDto;
import dev.ncns.sns.user.dto.response.LoginResponseDto;
import dev.ncns.sns.user.dto.response.UserResponseDto;
import dev.ncns.sns.user.dto.response.UserSummaryResponseDto;
import dev.ncns.sns.user.service.FollowService;
import dev.ncns.sns.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequiredArgsConstructor
@ComponentScan(basePackages = "dev.ncns.sns.common.exception")
@RequestMapping(value = "/api/user")
@RestController
public class UserController {

    @Value("${server.port}")
    private String port;

    private final UserService userService;
    private final FollowService followService;

    @Operation(summary = "회원가입")
    @NonAuthorize
    @PostMapping
    public ResponseEntity<Void> signUp(@Validated @RequestBody SignupRequestDto signupRequest) {
        userService.signUp(signupRequest);
        return ResponseEntity.successResponse(port);
    }

    @Operation(summary = "회원탈퇴")
    @DeleteMapping
    public ResponseEntity<Void> signOut() {
        userService.signOut();
        return ResponseEntity.successResponse(port, "Unregister Success!");
    }

    @Operation(summary = "사용자 정보 수정")
    @PatchMapping
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequestDto dto) {
        userService.updateProfile(dto);
        return ResponseEntity.successResponse(port);
    }

    @Operation(summary = "사용자 프로필 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable final Long userId) {
        UserResponseDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.successResponse(port, userInfo);
    }

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

    @Operation(summary = "이메일 중복 체크")
    @NonAuthorize
    @PostMapping("/email")
    public ResponseEntity<CheckResponseDto> checkDuplicateEmail(@RequestBody CheckEmailRequestDto checkEmailRequest) {
        CheckResponseDto checkResponse = userService.isDuplicateEmail(checkEmailRequest);
        return ResponseEntity.successResponse(port, checkResponse);
    }

    @Operation(summary = "계정 이름 중복 체크")
    @NonAuthorize
    @PostMapping("/account")
    public ResponseEntity<CheckResponseDto> checkDuplicateAccountName(@RequestBody CheckAccountRequestDto checkAccountRequest) {
        CheckResponseDto checkResponse = userService.isDuplicateAccountName(checkAccountRequest);
        return ResponseEntity.successResponse(port, checkResponse);
    }

    @ApiIgnore
    @NonAuthorize
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto loginResponse = userService.handleLoginRequest(loginRequest);
        return ResponseEntity.successResponse(port, loginResponse);
    }

    @ApiIgnore
    @NonAuthorize
    @PostMapping("/count/post")
    public ResponseEntity<Void> updateUserPostCount(@RequestBody UpdateUserPostCountDto dto) {
        userService.updatePostCount(dto);
        return ResponseEntity.successResponse(port);
    }

}
