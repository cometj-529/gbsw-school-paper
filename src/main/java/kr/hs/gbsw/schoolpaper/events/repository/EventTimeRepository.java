package kr.hs.gbsw.schoolpaper.events.repository;

import kr.hs.gbsw.schoolpaper.events.domain.EventTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTimeRepository extends JpaRepository<EventTimeEntity, String> {
}
