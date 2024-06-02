package kr.hs.gbsw.schoolpaper.student.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kr.hs.gbsw.schoolpaper.user.domain.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    private int grade;

    private int classNumber;

    private int studentNumber;

    private String name;

    @OneToOne(mappedBy = "student", cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private UserEntity user;


}
