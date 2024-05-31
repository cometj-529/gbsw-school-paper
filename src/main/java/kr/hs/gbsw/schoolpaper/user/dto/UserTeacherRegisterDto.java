package kr.hs.gbsw.schoolpaper.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTeacherRegisterDto {
    private String tel;

    private String password;

    private int teacherIdx;
}
