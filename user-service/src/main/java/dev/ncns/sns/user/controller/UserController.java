package dev.ncns.sns.user.controller;


import dev.ncns.sns.user.common.ResponseEntity;
import dev.ncns.sns.user.common.SecurityUtil;
import dev.ncns.sns.user.dto.ProfileUpdateRequestDto;
import dev.ncns.sns.user.dto.SignupRequestDto;
import dev.ncns.sns.user.dto.UserResponseDto;
import dev.ncns.sns.user.dto.UserSummaryResponseDto;
import dev.ncns.sns.user.service.FollowService;
import dev.ncns.sns.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {
    private final UserService userService;
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<?> signUp(@Validated @RequestBody SignupRequestDto signupDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            return new ResponseEntity(1001, "Validation failure", errors);
        }
        userService.signUp(signupDto.toEntity());
        return new ResponseEntity<>(1000,"success",null);
    }

    @DeleteMapping
    public ResponseEntity<?> signOut() throws Exception {
        return new ResponseEntity<>(1000,"Unregister Success!", userService.signOut());
    }

    @PatchMapping
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequestDto dto) {
        return new ResponseEntity(1000, "ok", userService.updateProfile(dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable final Long userId) throws Exception {
        UserResponseDto userInfo = userService.getUserInfo(userId);
        return new ResponseEntity(1000, "ok", userInfo);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<UserSummaryResponseDto> getFollowingList(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followingList = userService.getFollowingList(followService.getFollowingIdList(userId));
        return new ResponseEntity(1000,"following list",followingList);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<UserSummaryResponseDto> getFollowerList(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followerList = userService.getFollowerList(followService.getFollowerIdList(userId));
        return new ResponseEntity(1000,"follower list",followerList);
    }

    @PostMapping("/follow/{targetId}")
    public ResponseEntity<?> requestFollow(@PathVariable final Long targetId) {
        Long currentUserId = SecurityUtil.getCurrentMemberId();
        Long followData = followService.isFollowing(currentUserId,targetId);
        if (followData!=null) {
            followService.unFollow(followData);
            return new ResponseEntity(1000,"ok", "unfollow");
        }
        followService.requestFollow(currentUserId, targetId);
        return new ResponseEntity(1000,"ok", "follow");
    }
}