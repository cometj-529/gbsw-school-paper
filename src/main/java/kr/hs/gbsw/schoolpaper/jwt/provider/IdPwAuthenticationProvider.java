package kr.hs.gbsw.schoolpaper.jwt.provider;

import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import kr.hs.gbsw.schoolpaper.user.domain.UserEntity;
import kr.hs.gbsw.schoolpaper.user.repository.StudentRepository;
import kr.hs.gbsw.schoolpaper.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdPwAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String uuid = authentication.getPrincipal().toString();
        String password = (String) authentication.getCredentials();

        UserEntity user = userRepository.findById(UUID.fromString(uuid)).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(uuid, password, user.generateGrantedAuthorities());
        }

        throw new RuntimeException("인증 실패");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
