package kr.hs.gbsw.schoolpaper.user.repository;

import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
    Optional<StudentEntity> findByGradeAndClassNumberAndStudentNumber(int grade, int classNumber, int studentNumber);
}
