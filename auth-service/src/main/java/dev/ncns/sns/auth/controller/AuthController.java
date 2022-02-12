package dev.ncns.sns.auth.controller;

import dev.ncns.sns.auth.domain.AuthType;
import dev.ncns.sns.auth.dto.request.LoginRequestDto;
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

    @PostMapping("/local")
    public ResponseEntity<AuthResponseDto> localLogin(@Validated(LocalLoginValidation.class)
                                                      @RequestBody LoginRequestDto loginRequest,
                                                      HttpServletResponse httpServletResponse) {
        loginRequest.setAuthType(AuthType.LOCAL);
        return login(loginRequest, httpServletResponse);
    }

    @PostMapping("/account")
    public ResponseEntity<AuthResponseDto> accountLogin(@Validated(AccountLoginValidation.class)
                                                        @RequestBody LoginRequestDto loginRequest,
                                                        HttpServletResponse httpServletResponse) {
        loginRequest.setAuthType(AuthType.ACCOUNT);
        return login(loginRequest, httpServletResponse);
    }

    @PostMapping("/social")
    public ResponseEntity<AuthResponseDto> socialLogin(@Validated(SocialLoginValidation.class)
                                                       @RequestBody LoginRequestDto loginRequest,
                                                       HttpServletResponse httpServletResponse) {
        return login(loginRequest, httpServletResponse);
    }

    @Authorize
    @DeleteMapping
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader(Constants.AUTH_HEADER_KEY);
        Cookie refreshToken = cookieManager.getCookie(httpServletRequest, Constants.REFRESH_TOKEN_NAME);

        authService.discardToken(authorization, refreshToken.getValue());

        return ResponseEntity.successResponse(port);
    }

    private ResponseEntity<AuthResponseDto> login(LoginRequestDto loginRequest,
                                                  HttpServletResponse httpServletResponse) {
        ResponseEntity<LoginResponseDto> loginResponse = userFeignClient.login(loginRequest);
        AuthResponseDto authResponse = authService.issueToken(loginResponse.getData());

        Cookie refreshToken = cookieManager.createCookie(Constants.REFRESH_TOKEN_NAME, authResponse.getRefreshToken());
        httpServletResponse.addCookie(refreshToken);

        return ResponseEntity.successResponse(port, authResponse);
    }

}
