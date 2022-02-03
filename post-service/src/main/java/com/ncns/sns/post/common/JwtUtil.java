package com.ncns.sns.post.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${token.key}")
    private String tokenKey;

    private Claims parseClaims(String accessToken) {
        System.out.println(tokenKey);
        try {
            return Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        UserDetails principal = new User(claims.getSubject(), null, null);
        return new UsernamePasswordAuthenticationToken(principal, null, null);
    }
}