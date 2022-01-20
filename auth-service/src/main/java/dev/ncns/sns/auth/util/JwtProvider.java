package dev.ncns.sns.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    public static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 30; // 30minutes
    public static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 15; // 15days
    public static final String REFRESH_TOKEN_NAME = "RefreshToken";

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
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public long getExpirationDate(String token) {
        Date expiration = getClaims(token).getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
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
