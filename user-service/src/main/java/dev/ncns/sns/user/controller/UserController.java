package dev.ncns.sns.user.controller;

import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.common.domain.ResponseType;
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

@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@RestController
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<?> signUp(@Validated @RequestBody SignupRequestDto signupDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.failureResponse(ResponseType.USER_VALIDATION_FAILURE, errors);
        }
        userService.signUp(signupDto.toEntity());
        return ResponseEntity.successResponse();
    }

    @DeleteMapping
    public ResponseEntity<Void> signOut() throws Exception {
        userService.signOut();
        return ResponseEntity.successResponse("Unregister Success!");
    }

    @PatchMapping
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequestDto dto) {
        userService.updateProfile(dto);
        return ResponseEntity.successResponse();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable final Long userId) throws Exception {
        UserResponseDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.successResponse(userInfo);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowingList(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followingList = userService.getFollowingList(followService.getFollowingIdList(userId));
        return ResponseEntity.successResponse("following list", followingList);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserSummaryResponseDto>> getFollowerList(@PathVariable Long userId) {
        List<UserSummaryResponseDto> followerList = userService.getFollowerList(followService.getFollowerIdList(userId));
        return ResponseEntity.successResponse("follower list", followerList);
    }

    @PostMapping("/follow/{targetId}")
    public ResponseEntity<String> requestFollow(@PathVariable final Long targetId) {
        Long currentUserId = SecurityUtil.getCurrentMemberId();
        String data = followService.requestFollow(currentUserId, targetId);
        return ResponseEntity.successResponse(data);
    }

}
