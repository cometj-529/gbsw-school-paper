package kr.hs.gbsw.schoolpaper.events.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "event_time")
@Getter
@Setter
public class EventTimeEntity {
    @Id
    private String id;

    private String title;

    private LocalTime startTime;

    private LocalTime endTime;
}
