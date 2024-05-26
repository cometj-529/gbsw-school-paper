package kr.hs.gbsw.schoolpaper.user.service;

import kr.hs.gbsw.schoolpaper.jwt.IdPwAuthentication;
import kr.hs.gbsw.schoolpaper.jwt.JwtUtils;
import kr.hs.gbsw.schoolpaper.user.dto.UserLoginDto;
import kr.hs.gbsw.schoolpaper.user.dto.UserRegisterDto;
import kr.hs.gbsw.schoolpaper.user.entity.RoleEntity;
import kr.hs.gbsw.schoolpaper.user.entity.UserEntity;
import kr.hs.gbsw.schoolpaper.user.repository.RoleRepository;
import kr.hs.gbsw.schoolpaper.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository repository, RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public String login(UserLoginDto dto) {
        Authentication result = authenticationManager.authenticate(new IdPwAuthentication(dto.getTel(), dto.getPassword()));

        return createJwtToken(result);
    }

    public void register(UserRegisterDto dto) {
        if (repository.existsByTel(dto.getTel())) {
            throw new IllegalArgumentException("");
        }

        RoleEntity role = roleRepository.findById("USER").orElseGet(() -> {

            RoleEntity newRole = new RoleEntity();
            newRole.setId("USER");
            newRole.setTitle("사용자");

           roleRepository.save(newRole);

           return newRole;
        });

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(role);

        UserEntity entity = new UserEntity();
        entity.setName(dto.getName());
        entity.setTel(dto.getTel());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRoles(roles);

        userRepository.save(entity);
    }

    public String createJwtToken(Authentication authentication) {
        String tel = (String) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return jwtUtils.createJwt(tel, authorities);
    }
}
