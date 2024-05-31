package kr.hs.gbsw.schoolpaper.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStudentRegisterDto {
    private String tel;

    private String password;

    private int grade;

    private int classNumber;

    private int studentNumber;
}
