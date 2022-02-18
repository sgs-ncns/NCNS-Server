package dev.ncns.sns.search.controller;

import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.search.dto.request.CreateUserRequestDto;
import dev.ncns.sns.search.dto.request.UpdateUserRequestDto;
import dev.ncns.sns.search.dto.response.UserResponseDto;
import dev.ncns.sns.search.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/search/user")
@RestController
public class UserController extends ApiController {

    private final UserService userService;

    @Operation(summary = "[Only Server] 사용자 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        UserResponseDto userResponse = userService.getUser(userId);
        return getSuccessResponse(userResponse);
    }

    @Operation(summary = "[Only Server] 사용자 등록")
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDto createUserRequest) {
        userService.createUser(createUserRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "[Only Server] 사용자 수정", description = "계정 이름과 닉네임 수정")
    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequestDto updateUserRequest) {
        userService.updateUser(updateUserRequest);
        return getSuccessResponse();
    }

    @Operation(summary = "[Only Server] 사용자 삭제")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return getSuccessResponse();
    }

    @Operation(summary = "[Only Server] 계정 이름으로 사용자 조회", description = "키워드가 포함된 모든 사용자 조회")
    @GetMapping("/account-name")
    public ResponseEntity<List<UserResponseDto>> findUsersByAccountName(@RequestParam String keyword) {
        List<UserResponseDto> userResponse = userService.findUsersByAccountName(keyword);
        return getSuccessResponse(userResponse);
    }

    @Operation(summary = "[Only Server] 닉네임으로 사용자 조회", description = "키워드가 포함된 모든 사용자 조회")
    @GetMapping("/nickname")
    public ResponseEntity<List<UserResponseDto>> findUsersByNickname(@RequestParam String keyword) {
        List<UserResponseDto> userResponse = userService.findUsersByNickname(keyword);
        return getSuccessResponse(userResponse);
    }

}
