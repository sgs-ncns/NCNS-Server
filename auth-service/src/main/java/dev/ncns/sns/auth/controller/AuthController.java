package dev.ncns.sns.auth.controller;

import dev.ncns.sns.auth.dto.request.LoginRequestDto;
import dev.ncns.sns.auth.dto.response.AuthResponseDto;
import dev.ncns.sns.auth.dto.response.LoginResponseDto;
import dev.ncns.sns.auth.service.AuthService;
import dev.ncns.sns.auth.util.CookieManager;
import dev.ncns.sns.auth.util.JwtProvider;
import dev.ncns.sns.auth.dto.validate.AccountLoginValidation;
import dev.ncns.sns.auth.dto.validate.LocalLoginValidation;
import dev.ncns.sns.auth.dto.validate.SocialLoginValidation;
import dev.ncns.sns.common.domain.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;

    @PostMapping("/local")
    public ResponseEntity<AuthResponseDto> localLogin(@Validated(LocalLoginValidation.class)
                                                      @RequestBody LoginRequestDto loginRequest,
                                                      HttpServletResponse httpServletResponse) {
        LoginResponseDto loginResponse = new LoginResponseDto(1L); // TODO: user-service 이메일 로그인 호출
        return login(loginResponse, httpServletResponse);
    }

    @PostMapping("/account")
    public ResponseEntity<AuthResponseDto> accountLogin(@Validated(AccountLoginValidation.class)
                                                        @RequestBody LoginRequestDto loginRequest,
                                                        HttpServletResponse httpServletResponse) {
        LoginResponseDto loginResponse = new LoginResponseDto(1L); // TODO: user-service 계정 로그인 호출
        return login(loginResponse, httpServletResponse);
    }

    @PostMapping("/social")
    public ResponseEntity<AuthResponseDto> socialLogin(@Validated(SocialLoginValidation.class)
                                                       @RequestBody LoginRequestDto loginRequest,
                                                       HttpServletResponse httpServletResponse) {
        LoginResponseDto loginResponse = new LoginResponseDto(1L); // TODO: user-service 소셜 로그인 호출
        return login(loginResponse, httpServletResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken, // TODO: gateway-service 에서 필터로 거르기
                                       HttpServletRequest httpServletRequest) {
        Cookie refreshToken = cookieManager.getCookie(httpServletRequest, JwtProvider.REFRESH_TOKEN_NAME);
        accessToken = accessToken.replace("Bearer ", "");
        authService.discardToken(accessToken, refreshToken.getValue());
        return ResponseEntity.successResponse();
    }

    private ResponseEntity<AuthResponseDto> login(LoginResponseDto loginResponse,
                                                  HttpServletResponse httpServletResponse) {
        AuthResponseDto authResponse = authService.issueToken(loginResponse);

        Cookie refreshToken = cookieManager.createCookie(JwtProvider.REFRESH_TOKEN_NAME, authResponse.getRefreshToken());
        httpServletResponse.addCookie(refreshToken);

        return ResponseEntity.successResponse(authResponse);
    }

}
