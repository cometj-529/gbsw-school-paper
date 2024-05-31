package kr.hs.gbsw.schoolpaper.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private final String key;

    public JwtUtils(@Value("${jwt.key}") String key) {
        this.key = key;
    }

    public Jws<Claims> parseClaimsJws(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public boolean validate(String token) {
        try {
            parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String createJwt(String id, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> header = Map.of("alg", "HS512", "typ", "JWT");

        return Jwts.builder()
                .setHeader(header)
                .setSubject(id)
                .claim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String getUserTel(String token) {
        return parseClaimsJws(token).getBody().getSubject();
    }

    public List<String> getRoles(String token) {
        return parseClaimsJws(token).getBody().get("roles", List.class);
    }
}