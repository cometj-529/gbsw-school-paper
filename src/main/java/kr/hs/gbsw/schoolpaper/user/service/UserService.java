package kr.hs.gbsw.schoolpaper.user.service;

import kr.hs.gbsw.schoolpaper.jwt.IdPwAuthentication;
import kr.hs.gbsw.schoolpaper.jwt.JwtUtils;
import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import kr.hs.gbsw.schoolpaper.teacher.domain.TeacherEntity;
import kr.hs.gbsw.schoolpaper.teacher.repository.TeacherRepository;
import kr.hs.gbsw.schoolpaper.user.dto.UserLoginDto;
import kr.hs.gbsw.schoolpaper.user.dto.UserStudentRegisterDto;
import kr.hs.gbsw.schoolpaper.user.domain.RoleEntity;
import kr.hs.gbsw.schoolpaper.user.domain.UserEntity;
import kr.hs.gbsw.schoolpaper.user.dto.UserTeacherRegisterDto;
import kr.hs.gbsw.schoolpaper.user.repository.RoleRepository;
import kr.hs.gbsw.schoolpaper.user.repository.StudentRepository;
import kr.hs.gbsw.schoolpaper.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TeacherRepository teacherRepository;
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

        UserEntity user = repository.findByTel(dto.getTel()).orElseThrow(() -> new IllegalArgumentException(""));

        Authentication result = authenticationManager.authenticate(new IdPwAuthentication(user.getUuid(), dto.getPassword()));

        return createJwtToken(result);
    }

    public void register(UserStudentRegisterDto dto) {
        if (repository.existsByTel(dto.getTel())) {
            throw new IllegalArgumentException("");
        }

        StudentEntity student = studentRepository.findByGradeAndClassNumberAndStudentNumber(dto.getGrade(), dto.getClassNumber(), dto.getStudentNumber()).orElseThrow(() -> new IllegalArgumentException(""));

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
        entity.setUuid(UUID.randomUUID());
        entity.setTel(dto.getTel());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRoles(roles);
        entity.setStudent(student);

        userRepository.save(entity);
    }

    public String createJwtToken(Authentication authentication) {
        String uuid = (String) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return jwtUtils.createJwt(uuid, authorities);
    }

    public UserEntity getMy(String uuid) {
        UserEntity user = repository.findById(UUID.fromString(uuid)).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        return user;
    }

    public void adminRegister(UserTeacherRegisterDto dto) {
        if (repository.existsByTel(dto.getTel())) {
            throw new IllegalArgumentException("");
        }

        TeacherEntity teacher = teacherRepository.findById(dto.getTeacherIdx()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 선생님"));

        RoleEntity role = roleRepository.findById("ADMIN").orElseGet(() -> {

            RoleEntity newRole = new RoleEntity();
            newRole.setId("ADMIN");
            newRole.setTitle("관리자");

            roleRepository.save(newRole);

            return newRole;
        });

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(role);

        UserEntity entity = new UserEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setTel(dto.getTel());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRoles(roles);
        entity.setTeacher(teacher);

        userRepository.save(entity);
    }
}
