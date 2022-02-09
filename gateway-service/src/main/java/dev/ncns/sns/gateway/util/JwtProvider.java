package dev.ncns.sns.gateway.util;

import dev.ncns.sns.gateway.domain.ResponseType;
import dev.ncns.sns.gateway.exception.BadRequestException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtProvider {

    private final String secretKey;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
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

    public String getBlackListTokenValue(String id) {
        return Constants.BLACKLIST_TOKEN_NAME + "[" + id + "]";
    }

    public String getAccessToken(String authorization) {
        if (!authorization.startsWith(Constants.AUTH_HEADER_VALUE_PREFIX)) {
            throw new BadRequestException(ResponseType.JWT_HEADER_PREFIX);
        }
        return authorization.replace(Constants.AUTH_HEADER_VALUE_PREFIX, "");
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

}
