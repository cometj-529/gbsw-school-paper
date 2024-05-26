package kr.hs.gbsw.schoolpaper.jwt.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hs.gbsw.schoolpaper.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private static final String AUTHORIZATION = "Authorization";
    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader(AUTHORIZATION);
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String bearerToken = authorization.substring(7).trim();

                // JWT 파싱해서 검증하고 사용자 아이디 가져오기
                String userId = jwtUtils.getUserTel(bearerToken);
                List<String> roles = jwtUtils.getRoles(bearerToken);
                Collection<? extends GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userId, bearerToken, authorities));
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();

            if (!(e instanceof JwtException) && !(e instanceof BadCredentialsException)) {
                log.error("JWT processing failed: {}", e.getMessage(), e);
            }
        }

        filterChain.doFilter(request, response);
        MDC.clear();
    }
}
