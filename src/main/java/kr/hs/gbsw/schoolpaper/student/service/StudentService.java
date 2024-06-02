package kr.hs.gbsw.schoolpaper.student.service;

import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import kr.hs.gbsw.schoolpaper.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository repository;

    public List<StudentEntity> findByGradeAndClassNumber(int grade, int classNumber) {
        return repository.findByGradeAndClassNumber(grade, classNumber);
    }
}
