package kr.hs.gbsw.schoolpaper.schooloutgo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SchoolOutGoCreateDto {
    private String eventTimeId;
    private LocalDate useDate;
}
