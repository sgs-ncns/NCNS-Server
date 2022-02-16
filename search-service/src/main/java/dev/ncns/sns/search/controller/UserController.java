package dev.ncns.sns.search.controller;

import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.search.dto.request.CreateUserRequestDto;
import dev.ncns.sns.search.dto.response.UserResponseDto;
import dev.ncns.sns.search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value = "/api/search/user")
@RestController
public class UserController extends ApiController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        UserResponseDto userResponse = userService.getUser(userId);
        return getSuccessResponse(userResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDto createUserRequest) {
        userService.createUser(createUserRequest);
        return getSuccessResponse();
    }

}
