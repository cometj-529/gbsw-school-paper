package kr.hs.gbsw.schoolpaper.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private final String key;
    private final String outGoKey;

    public JwtUtils(@Value("${jwt.key}") String key, @Value("${jwt.out-go-key}") String outGoKey) {
        this.key = key;
        this.outGoKey = outGoKey;
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

    public String createJwt(String uuid, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> header = Map.of("alg", "HS512", "typ", "JWT");

        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000 * 60 * 60 * 24);

        return Jwts.builder()
                .setHeader(header)
                .setSubject(uuid)
                .claim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String createOutGoJwt(int outGoIdx) {
        Map<String, Object> header = Map.of("alg", "HS512", "typ", "JWT");

        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000 * 60 * 5);

        return Jwts.builder()
                .setHeader(header)
                .setSubject(String.valueOf(outGoIdx))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, outGoKey)
                .compact();
    }

    public String getSubject(String token) {
        return parseClaimsJws(token).getBody().getSubject();
    }

    public List<String> getRoles(String token) {
        return parseClaimsJws(token).getBody().get("roles", List.class);
    }
}