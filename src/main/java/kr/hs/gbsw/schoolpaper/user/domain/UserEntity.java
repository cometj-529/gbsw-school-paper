package kr.hs.gbsw.schoolpaper.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import kr.hs.gbsw.schoolpaper.teacher.domain.TeacherEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity {
    @Id
    private UUID uuid;

    @Column(unique = true)
    private String tel;

    @JsonIgnore
    private String password;

    @ManyToMany(cascade = {CascadeType.REMOVE})
    private List<RoleEntity> roles;

    @OneToOne()
    private StudentEntity student;

    @OneToOne()
    private TeacherEntity teacher;

    public List<GrantedAuthority> generateGrantedAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getId()))
                .collect(Collectors.toList());
    }

    public boolean isStudent() {
        return student != null;
    }

    public boolean isTeacher() {
        return teacher != null;
    }
}
