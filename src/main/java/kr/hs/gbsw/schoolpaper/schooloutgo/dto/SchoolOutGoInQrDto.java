package kr.hs.gbsw.schoolpaper.schooloutgo.dto;

import kr.hs.gbsw.schoolpaper.events.domain.EventTimeEntity;
import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SchoolOutGoInQrDto {
    private int idx;
    private EventTimeEntity eventTime;
    private LocalDate useDate;
    private LocalDateTime usedChangeDateTime;
    private StudentEntity student;
    private boolean isAllow;
    private boolean isUsed;
    private boolean isExpired;
    private boolean isAvailable;
    private String qrCode;
}
