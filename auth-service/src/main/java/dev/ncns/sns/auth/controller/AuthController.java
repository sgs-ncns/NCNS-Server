package dev.ncns.sns.auth.controller;

import dev.ncns.sns.auth.domain.AuthType;
import dev.ncns.sns.auth.dto.request.LoginRequestDto;
import dev.ncns.sns.auth.dto.response.AuthLoginResponseDto;
import dev.ncns.sns.auth.dto.response.AuthResponseDto;
import dev.ncns.sns.auth.dto.response.LoginResponseDto;
import dev.ncns.sns.auth.dto.validate.AccountLoginValidation;
import dev.ncns.sns.auth.dto.validate.LocalLoginValidation;
import dev.ncns.sns.auth.dto.validate.SocialLoginValidation;
import dev.ncns.sns.auth.service.AuthService;
import dev.ncns.sns.auth.util.CookieManager;
import dev.ncns.sns.common.annotation.Authorize;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.common.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@ComponentScan(basePackages = "dev.ncns.sns.common.exception")
@RequestMapping(value = "/api/auth")
@RestController
public class AuthController {

    @Value("${server.port}")
    private String port;

    private final AuthService authService;
    private final CookieManager cookieManager;

    private final UserFeignClient userFeignClient;

    @Operation(summary = "자체 로그인", description = "email + password 로그인")
    @PostMapping("/local")
    public ResponseEntity<AuthLoginResponseDto> localLogin(@Validated(LocalLoginValidation.class)
                                                           @RequestBody LoginRequestDto loginRequest,
                                                           HttpServletResponse httpServletResponse) {
        loginRequest.setAuthType(AuthType.LOCAL);
        return login(loginRequest, httpServletResponse);
    }

    @Operation(summary = "계정 이름 로그인", description = "account name + password 로그인")
    @PostMapping("/account")
    public ResponseEntity<AuthLoginResponseDto> accountLogin(@Validated(AccountLoginValidation.class)
                                                             @RequestBody LoginRequestDto loginRequest,
                                                             HttpServletResponse httpServletResponse) {
        loginRequest.setAuthType(AuthType.ACCOUNT);
        return login(loginRequest, httpServletResponse);
    }

    @Operation(summary = "소셜 로그인", description = "email + auth type(APPLE|GOOGLE) 로그인")
    @PostMapping("/social")
    public ResponseEntity<AuthLoginResponseDto> socialLogin(@Validated(SocialLoginValidation.class)
                                                            @RequestBody LoginRequestDto loginRequest,
                                                            HttpServletResponse httpServletResponse) {
        return login(loginRequest, httpServletResponse);
    }

    @Operation(summary = "로그아웃")
    @Authorize
    @DeleteMapping
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse) {
        String authorization = httpServletRequest.getHeader(Constants.AUTH_HEADER_KEY);
        Cookie refreshToken = cookieManager.getCookie(httpServletRequest, Constants.REFRESH_TOKEN_NAME);

        authService.discardToken(authorization, refreshToken.getValue());
        httpServletResponse.addCookie(cookieManager.deleteCookie(refreshToken));

        return ResponseEntity.successResponse(port);
    }

    private ResponseEntity<AuthLoginResponseDto> login(LoginRequestDto loginRequest,
                                                       HttpServletResponse httpServletResponse) {
        ResponseEntity<LoginResponseDto> loginResponse = userFeignClient.login(loginRequest);
        AuthResponseDto authResponse = authService.issueToken(loginResponse.getData());
        AuthLoginResponseDto authLoginResponse = AuthLoginResponseDto.of(authResponse, loginResponse.getData());

        Cookie refreshToken = cookieManager.createCookie(Constants.REFRESH_TOKEN_NAME, authResponse.getRefreshToken());
        httpServletResponse.addCookie(refreshToken);

        return ResponseEntity.successResponse(port, authLoginResponse);
    }

}
