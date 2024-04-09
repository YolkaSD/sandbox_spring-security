package com.example.config.util;

import com.example.config.property.JwtProperty;
import com.example.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    private final JwtProperty jwtProperty;

    public String generateToken(User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", roles);

        Date issuedAt = new Date();
        Date expire = new Date(issuedAt.getTime() + jwtProperty.lifetime().toMillis());
        Key key = Keys.hmacShaKeyFor(jwtProperty.secret().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(issuedAt)
                .expiration(expire)
                .signWith(key)
                .compact();
    }

    public String getUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        List<?> authorities = getClaimsFromToken(token).get("authorities", List.class);
        if (authorities.stream().allMatch(obj -> obj instanceof String)) {
            return authorities.stream()
                    .map(obj -> (String) obj)
                    .toList();
        } else {
            throw new ClassCastException("Not all elements in the list are strings");
        }
    }

    private Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperty.secret().getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
