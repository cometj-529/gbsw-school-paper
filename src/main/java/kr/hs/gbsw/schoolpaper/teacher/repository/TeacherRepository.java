package kr.hs.gbsw.schoolpaper.teacher.repository;

import kr.hs.gbsw.schoolpaper.teacher.domain.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Integer> {
}
