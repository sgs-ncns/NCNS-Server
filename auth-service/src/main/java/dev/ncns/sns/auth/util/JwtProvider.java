package dev.ncns.sns.auth.util;

import dev.ncns.sns.common.domain.ResponseType;
import dev.ncns.sns.common.exception.BadRequestException;
import dev.ncns.sns.common.util.Constants;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@RefreshScope
@Component
public class JwtProvider {

    public static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 30; // 30minutes
    public static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 15; // 15days

    private final String secretKey;

    /**
     * 저희만의 JWT Secret 값을 설정 파일에서 읽어 생성합니다.
     */
    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * 30분의 유효기간을 갖는 AccessToken을 생성합니다.
     */
    public String createAccessToken(String subject) {
        return createToken(subject, ACCESS_TOKEN_VALIDITY);
    }

    /**
     * 15일의 유효기간을 갖는 RefreshToken을 생성합니다.
     */
    public String createRefreshToken(String subject) {
        return createToken(subject, REFRESH_TOKEN_VALIDITY);
    }

    public ResponseType validateToken(String token) {
        ResponseType responseType;
        try {
            getClaims(token);
            responseType = ResponseType.SUCCESS;
        } catch (MalformedJwtException exception) {
            responseType = ResponseType.JWT_MALFORMED;
        } catch (UnsupportedJwtException exception) {
            responseType = ResponseType.JWT_UNSUPPORTED;
        } catch (SignatureException exception) {
            responseType = ResponseType.JWT_SIGNATURE;
        } catch (ExpiredJwtException exception) {
            responseType = ResponseType.JWT_EXPIRED;
        } catch (IllegalArgumentException exception) {
            responseType = ResponseType.JWT_NULL_OR_EMPTY;
        }
        return responseType;
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public String getRefreshTokenKey(String id) {
        return Constants.REFRESH_TOKEN_NAME + "[" + id + "]";
    }

    public String getBlackListTokenValue(String id) {
        return Constants.BLACKLIST_TOKEN_NAME + "[" + id + "]";
    }

    public long getExpirationDate(String token) {
        Date expiration = getClaims(token).getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }

    /**
     * Authorization Header에서 읽어온 값을 검증하고 AccessToken 값만 가져옵니다.
     */
    public String getAccessToken(String authorization) {
        if (!authorization.startsWith(Constants.AUTH_HEADER_VALUE_PREFIX)) {
            throw new BadRequestException(ResponseType.JWT_HEADER_PREFIX);
        }
        return authorization.replace(Constants.AUTH_HEADER_VALUE_PREFIX, "");
    }

    /**
     * RefreshToken의 만료기간이 총 유효기간의 1/3보다 적게 남았는지 확인합니다. (RefreshToken 연장을 위함)
     */
    public boolean isRefreshTokenExpiration(String token) {
        long expirationDate = getExpirationDate(token);
        return expirationDate <= (REFRESH_TOKEN_VALIDITY / 3);
    }

    private String createToken(String subject, long tokenValidity) {
        Claims claims = Jwts.claims().setSubject(subject);
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + tokenValidity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

}
