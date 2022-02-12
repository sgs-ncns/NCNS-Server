package dev.ncns.sns.user.controller;

import dev.ncns.sns.common.annotation.NonAuthorize;
import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.user.dto.request.*;
import dev.ncns.sns.user.dto.response.CheckResponseDto;
import dev.ncns.sns.user.dto.response.LoginResponseDto;
import dev.ncns.sns.user.dto.response.UserResponseDto;
import dev.ncns.sns.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@RestController
public class UserController extends ApiController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @NonAuthorize
    @PostMapping
    public ResponseEntity<Void> signUp(@Validated @RequestBody SignUpRequestDto signUpRequest) {
        userService.signUp(signUpRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "회원탈퇴")
    @DeleteMapping
    public ResponseEntity<Void> signOut() {
        userService.signOut();
        return getSuccessResponse();
    }

    @Operation(summary = "사용자 정보 수정")
    @PatchMapping
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileRequestDto updateProfileRequest) {
        userService.updateProfile(updateProfileRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "사용자 프로필 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable final Long userId) {
        UserResponseDto userResponse = userService.getUserInfo(userId);
        return getSuccessResponse(userResponse);
    }

    @Operation(summary = "이메일 중복 체크")
    @NonAuthorize
    @PostMapping("/email")
    public ResponseEntity<CheckResponseDto> checkDuplicateEmail(@RequestBody CheckEmailRequestDto checkEmailRequest) {
        CheckResponseDto checkResponse = userService.isDuplicateEmail(checkEmailRequest);
        return getSuccessResponse(checkResponse);
    }

    @Operation(summary = "계정 이름 중복 체크")
    @NonAuthorize
    @PostMapping("/account")
    public ResponseEntity<CheckResponseDto> checkDuplicateAccountName(@RequestBody CheckAccountRequestDto checkAccountRequest) {
        CheckResponseDto checkResponse = userService.isDuplicateAccountName(checkAccountRequest);
        return getSuccessResponse(checkResponse);
    }

    @ApiIgnore
    @NonAuthorize
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto loginResponse = userService.handleLoginRequest(loginRequest);
        return getSuccessResponse(loginResponse);
    }

    @ApiIgnore
    @NonAuthorize
    @PostMapping("/count/post")
    public ResponseEntity<Void> updateUserPostCount(@RequestBody UpdateUserPostCountDto dto) {
        userService.updatePostCount(dto);
        return getSuccessResponse();
    }

}
