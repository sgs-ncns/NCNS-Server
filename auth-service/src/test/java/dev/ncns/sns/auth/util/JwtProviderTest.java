package dev.ncns.sns.auth.util;

import dev.ncns.sns.common.domain.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtProviderTest {

    private String userId;

    @Autowired
    private JwtProvider jwtProvider;

    @BeforeEach
    private void beforeTest() {
        userId = Long.toString(1L);
    }

    @Test
    public void createAccessTokenTest() {
        String accessToken = jwtProvider.createAccessToken(userId);
        System.out.println("AccessToken : " + accessToken);
    }

    @Test
    public void createRefreshTokenTest() {
        String refreshToken = jwtProvider.createRefreshToken(userId);
        System.out.println("RefreshToken : " + refreshToken);
    }

    @Test
    public void validateTokenTest() {
        String accessToken = jwtProvider.createAccessToken(userId);
        ResponseType responseType = jwtProvider.validateToken(accessToken);
        System.out.println("Validate Result : " + (responseType.equals(ResponseType.SUCCESS) ? "Usable" : "Unusable"));
    }

    @Test
    public void getSubjectTest() {
        String refreshToken = jwtProvider.createRefreshToken(userId);
        String subject = jwtProvider.getSubject(refreshToken);
        System.out.println("Subject : " + subject + " (" + userId.equals(subject) + ")");
    }

}
