package dev.ncns.sns.auth.controller;

import dev.ncns.sns.auth.domain.AuthType;
import dev.ncns.sns.auth.dto.request.LoginRequestDto;
import dev.ncns.sns.auth.dto.request.UpdateAccessAtRequestDto;
import dev.ncns.sns.auth.dto.response.AuthLoginResponseDto;
import dev.ncns.sns.auth.dto.response.AuthResponseDto;
import dev.ncns.sns.auth.dto.response.LoginResponseDto;
import dev.ncns.sns.auth.dto.validate.AccountLoginValidation;
import dev.ncns.sns.auth.dto.validate.LocalLoginValidation;
import dev.ncns.sns.auth.dto.validate.SocialLoginValidation;
import dev.ncns.sns.auth.service.AuthService;
import dev.ncns.sns.auth.util.CookieManager;
import dev.ncns.sns.common.annotation.Authorize;
import dev.ncns.sns.common.controller.ApiController;
import dev.ncns.sns.common.domain.ResponseEntity;
import dev.ncns.sns.common.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
@RestController
public class AuthController extends ApiController {

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

    @Operation(summary = "로그아웃", description = "Header Authorization에 담긴 AccessToken으로 로그아웃")
    @Authorize
    @DeleteMapping
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse) {
        String authorization = httpServletRequest.getHeader(Constants.AUTH_HEADER_KEY);
        Cookie refreshToken = cookieManager.getCookie(httpServletRequest, Constants.REFRESH_TOKEN_NAME);

        /**
         * AccessToken과 RefreshToken을 사용하지 못하도록 처리하고, Cookie에서도 RefreshToken을 제거합니다.
         */
        authService.discardToken(authorization, refreshToken.getValue());
        httpServletResponse.addCookie(cookieManager.deleteCookie(refreshToken));

        return getSuccessResponse();
    }

    @Operation(summary = "AccessToken 재발급", description = "Cookie에 저장된 RefreshToken 검증 후 AccessToken 재발급")
    @GetMapping
    public ResponseEntity<AuthResponseDto> reissue(HttpServletRequest httpServletRequest,
                                                   HttpServletResponse httpServletResponse) {
        /**
         * Cookie에 담긴 RefreshToken을 가져옵니다.
         * RefreshToken으로 사용자의 AccessToken을 재발급합니다.
         */
        Cookie refreshToken = cookieManager.getCookie(httpServletRequest, Constants.REFRESH_TOKEN_NAME);
        AuthResponseDto authResponse = authService.reissueToken(refreshToken.getValue());

        /**
         * RefreshToken 유효기간이 1/3도 남지 않아 재발급된 경우,
         * Cookie에 RefreshToken을 다시 저장해줍니다.
         */
        if (!refreshToken.getValue().equals(authResponse.getRefreshToken())) {
            refreshToken = cookieManager.createCookie(Constants.REFRESH_TOKEN_NAME, authResponse.getRefreshToken());
            httpServletResponse.addCookie(refreshToken);
        }

        /**
         * 토큰 재발급시 사용자의 최근접속일을 업데이트합니다.
         */
        Long userId = Long.parseLong(authService.getUserId(refreshToken.getValue()));
        userFeignClient.updateUserAccessAt(UpdateAccessAtRequestDto.of(userId));

        return getSuccessResponse(authResponse);
    }

    private ResponseEntity<AuthLoginResponseDto> login(LoginRequestDto loginRequest,
                                                       HttpServletResponse httpServletResponse) {
        /**
         * User Server login 요청을 호출하여 사용자의 로그인 정보를 검증하고 정보를 가져옵니다.
         * User 최소 정보(userId)가 담긴 accessToken과 refreshToken을 발급합니다.
         */
        ResponseEntity<LoginResponseDto> loginResponse = userFeignClient.login(loginRequest);
        AuthResponseDto authResponse = authService.issueToken(loginResponse.getData());
        AuthLoginResponseDto authLoginResponse = AuthLoginResponseDto.of(authResponse, loginResponse.getData());

        /**
         * Cookie를 생성하여 RefreshToken을 저장합니다.
         */
        Cookie refreshToken = cookieManager.createCookie(Constants.REFRESH_TOKEN_NAME, authResponse.getRefreshToken());
        httpServletResponse.addCookie(refreshToken);

        return getSuccessResponse(authLoginResponse);
    }

}
