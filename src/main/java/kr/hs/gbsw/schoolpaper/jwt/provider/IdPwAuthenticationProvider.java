package kr.hs.gbsw.schoolpaper.jwt.provider;

import kr.hs.gbsw.schoolpaper.jwt.IdPwAuthentication;
import kr.hs.gbsw.schoolpaper.user.entity.UserEntity;
import kr.hs.gbsw.schoolpaper.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdPwAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String tel = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserEntity user = userRepository.findByTel(tel).orElseThrow(() -> new RuntimeException("사용자가 없습니다."));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(tel, password, user.generateGrantedAuthorities());
        }

        throw new RuntimeException("인증 실패");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication); // 수정된 부분
    }
}
