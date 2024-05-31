package kr.hs.gbsw.schoolpaper.schooloutgo.domain;

import jakarta.persistence.*;
import kr.hs.gbsw.schoolpaper.events.domain.EventTimeEntity;
import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "school_out_go", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"event_time_id", "student_id"})
})
@Getter
@Setter
public class SchoolOutGoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @ManyToOne
    @JoinColumn(name = "event_time_id")
    private EventTimeEntity eventTime;

    private LocalDate useDate;

    private LocalDateTime usedChangeDateTime;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    private boolean isAllow = false;

    private boolean isUsed = false;

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime eventEndDateTime = LocalDateTime.of(useDate, eventTime.getEndTime());

        return now.isAfter(eventEndDateTime);
    }

    public boolean isAvailable() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime eventStartDateTime = LocalDateTime.of(useDate, eventTime.getStartTime());
        LocalDateTime eventEndDateTime = LocalDateTime.of(useDate, eventTime.getEndTime());

        return now.isAfter(eventStartDateTime) && now.isBefore(eventEndDateTime);
    }

}
