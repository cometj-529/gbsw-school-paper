package kr.hs.gbsw.schoolpaper.student.controller;

import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import kr.hs.gbsw.schoolpaper.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@ResponseBody
@RequiredArgsConstructor
public class StudentController {
    private final StudentService service;

    @GetMapping("/{grade}/{classNumber}")
    public List<StudentEntity> findByGradeAndClassNumber(@PathVariable("grade") int grade, @PathVariable("classNumber") int classNumber) {
        return service.findByGradeAndClassNumber(grade, classNumber);
    }
}
