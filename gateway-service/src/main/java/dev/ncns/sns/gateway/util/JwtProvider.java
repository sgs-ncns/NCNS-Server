package dev.ncns.sns.gateway.util;

import dev.ncns.sns.common.util.Constants;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    private final String secretKey;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
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

    public String getBlackListTokenValue(String id) {
        return Constants.BLACKLIST_TOKEN_NAME + "[" + id + "]";
    }

    public String getAccessToken(String authorization) {
        if (!authorization.startsWith(Constants.AUTH_HEADER_VALUE_PREFIX)) {
            throw new IllegalArgumentException("Not start with Bearer");
        }
        return authorization.replace(Constants.AUTH_HEADER_VALUE_PREFIX, "");
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
