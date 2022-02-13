package dev.ncns.sns.user.controller;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.user.common.SecurityUtil;
import dev.ncns.sns.user.dto.request.LoginRequestDto;
import dev.ncns.sns.user.dto.request.ProfileUpdateRequestDto;
import dev.ncns.sns.user.dto.request.SignupRequestDto;
import dev.ncns.sns.user.dto.request.UpdateUserPostCountDto;
import dev.ncns.sns.user.dto.response.LoginResponseDto;
import dev.ncns.sns.user.dto.response.UserResponseDto;
import dev.ncns.sns.user.dto.response.UserSummaryResponseDto;
import dev.ncns.sns.user.service.FollowService;
import dev.ncns.sns.user.service.UserService;
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

    @NonAuthorize
    @PostMapping
    public ResponseEntity<Void> signUp(@Validated @RequestBody SignupRequestDto signupRequest) {
        userService.signUp(signupRequest);
        return ResponseEntity.successResponse(port);
    }

    @DeleteMapping
    public ResponseEntity<Void> signOut() {
        userService.signOut();
        return ResponseEntity.successResponse(port, "Unregister Success!");
    }

    @PatchMapping
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequestDto dto) {
        userService.updateProfile(dto);
        return ResponseEntity.successResponse(port);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable final Long userId) {
        UserResponseDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.successResponse(port, userInfo);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowingList(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followingList = userService.getFollowingList(followService.getFollowingIdList(userId));
        return ResponseEntity.successResponse(port, "following list", followingList);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowerList(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followerList = userService.getFollowerList(followService.getFollowerIdList(userId));
        return ResponseEntity.successResponse(port, "follower list", followerList);
    }

    @PostMapping("/follow/{targetId}")
    public ResponseEntity<String> requestFollow(@PathVariable final Long targetId) {
        Long currentUserId = SecurityUtil.getCurrentMemberId();
        String data = followService.requestFollow(currentUserId, targetId);
        return ResponseEntity.successResponse(port, data);
    }

    @ApiIgnore
    @NonAuthorize
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto loginResponse = userService.handleLoginRequest(loginRequest);
        return ResponseEntity.successResponse(port, loginResponse);
    }

    @NonAuthorize
    @PostMapping("/count/post")
    public ResponseEntity<Void> updateUserPostCount(@RequestBody UpdateUserPostCountDto dto) {
        userService.updatePostCount(dto);
        return ResponseEntity.successResponse(port);
    }

}
