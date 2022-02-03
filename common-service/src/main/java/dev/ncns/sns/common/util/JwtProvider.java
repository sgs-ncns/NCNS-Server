package dev.ncns.sns.common.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    public static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 30; // 30minutes
    public static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 15; // 15days

    private final String secretKey;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String subject) {
        return createToken(subject, ACCESS_TOKEN_VALIDITY);
    }

    public String createRefreshToken(String subject) {
        return createToken(subject, REFRESH_TOKEN_VALIDITY);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return !isExpirationDate(claims.getExpiration());
        } catch (MalformedJwtException exception) {
            System.out.println("위조된 토큰입니다.");
        } catch (UnsupportedJwtException exception) {
            System.out.println("지원하지 않는 토큰입니다.");
        } catch (SignatureException exception) {
            System.out.println("시그니처 검증에 실패한 토큰입니다.");
        } catch (ExpiredJwtException exception) {
            System.out.println("만료된 토큰입니다.");
        } catch (IllegalArgumentException exception) {
            System.out.println("잘못된 토큰입니다.");
        }
        return false;
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

    public String getAccessToken(String authorization) {
        if (!authorization.startsWith(Constants.AUTH_HEADER_VALUE_PREFIX)) {
            throw new IllegalArgumentException("Not start with Bearer");
        }
        return authorization.replace(Constants.AUTH_HEADER_VALUE_PREFIX, "");
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

    private boolean isExpirationDate(Date expiration) {
        return expiration.before(new Date());
    }

}
