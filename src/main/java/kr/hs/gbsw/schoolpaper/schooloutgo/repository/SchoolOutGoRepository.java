package kr.hs.gbsw.schoolpaper.schooloutgo.repository;

import kr.hs.gbsw.schoolpaper.schooloutgo.domain.SchoolOutGoEntity;
import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolOutGoRepository extends JpaRepository<SchoolOutGoEntity, Integer> {
    List<SchoolOutGoEntity> findAllByStudentGradeAndStudentClassNumber(int grade, int classNumber);

    List<SchoolOutGoEntity> findByStudent(StudentEntity student);
}
