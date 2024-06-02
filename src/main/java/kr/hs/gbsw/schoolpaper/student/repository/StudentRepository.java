package kr.hs.gbsw.schoolpaper.student.repository;

import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
    List<StudentEntity> findByGradeAndClassNumber(int grade, int classNumber);
    Optional<StudentEntity> findByGradeAndClassNumberAndStudentNumber(int grade, int classNumber, int studentNumber);
}
