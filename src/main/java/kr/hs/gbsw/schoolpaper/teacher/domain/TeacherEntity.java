package kr.hs.gbsw.schoolpaper.teacher.domain;

import jakarta.persistence.*;
import kr.hs.gbsw.schoolpaper.user.domain.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "teacher")
@Getter
@Setter
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    private int grade;

    private int classNumber;

    private String name;

    @OneToOne(mappedBy = "teacher", cascade = {CascadeType.REMOVE})
    private UserEntity user;
}
