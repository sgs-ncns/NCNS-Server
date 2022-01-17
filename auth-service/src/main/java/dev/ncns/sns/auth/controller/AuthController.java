package dev.ncns.sns.auth.controller;

import dev.ncns.sns.auth.common.ResponseEntity;
import dev.ncns.sns.auth.dto.request.LocalLoginRequestDto;
import dev.ncns.sns.auth.dto.response.AuthResponseDto;
import dev.ncns.sns.auth.dto.response.LoginResponseDto;
import dev.ncns.sns.auth.service.AuthService;
import dev.ncns.sns.auth.util.CookieManager;
import dev.ncns.sns.auth.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;

    @PostMapping("/local")
    public ResponseEntity<AuthResponseDto> localLogin(@RequestBody @Valid LocalLoginRequestDto loginRequest,
                                                      HttpServletResponse httpServletResponse) {
        LoginResponseDto loginResponse = new LoginResponseDto(1L); // TODO: user-service 호출
        AuthResponseDto authResponse = authService.issueToken(loginResponse);

        Cookie refreshToken = cookieManager.createCookie(JwtProvider.REFRESH_TOKEN_NAME, authResponse.getRefreshToken());
        httpServletResponse.addCookie(refreshToken);

        return ResponseEntity.successResponse(authResponse);
    }

}
