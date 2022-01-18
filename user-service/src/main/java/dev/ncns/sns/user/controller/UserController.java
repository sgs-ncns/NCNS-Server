package dev.ncns.sns.user.controller;


import dev.ncns.sns.user.common.ResponseEntity;
import dev.ncns.sns.user.dto.ProfileUpdateRequestDto;
import dev.ncns.sns.user.dto.SignupRequestDto;
import dev.ncns.sns.user.dto.UserResponseDto;
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

    @GetMapping("/{account}")
    public ResponseEntity<?> getProfile(@PathVariable final String account) throws Exception {
        UserResponseDto user = userService.getUserInfo(account);
        return new ResponseEntity(1000, "ok", user);
    }
}