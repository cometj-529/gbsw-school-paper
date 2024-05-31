package kr.hs.gbsw.schoolpaper.schooloutgo.controller;

import kr.hs.gbsw.schoolpaper.common.AuthUserId;
import kr.hs.gbsw.schoolpaper.schooloutgo.domain.SchoolOutGoEntity;
import kr.hs.gbsw.schoolpaper.schooloutgo.dto.SchoolOutGoByClassDto;
import kr.hs.gbsw.schoolpaper.schooloutgo.dto.SchoolOutGoCreateDto;
import kr.hs.gbsw.schoolpaper.schooloutgo.service.SchoolOutGoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outgo/school")
@ResponseBody
@RequiredArgsConstructor
public class SchoolOutGoController {

    private final SchoolOutGoService service;

    @GetMapping("/{grade}/{classNumber}")
    public List<SchoolOutGoEntity> getByClassInfo(@PathVariable("grade") int grade, @PathVariable("classNumber") int classNumber, @AuthUserId String studentInfo) {

        List<SchoolOutGoEntity> results = service.getByClassInfo(grade, classNumber, studentInfo);

        return results;
    }

    @GetMapping("/my")
    public List<SchoolOutGoEntity> getMy(@AuthUserId String studentInfo) {
        return service.getMy(studentInfo);
    }

    @PostMapping("")
    public void createOutGo(@RequestBody SchoolOutGoCreateDto dto, @AuthUserId String studentInfo) {
        service.createOutGo(dto, studentInfo);
    }

    @DeleteMapping("/{idx}")
    public void removeOutGo(@PathVariable("idx") int outGoIdx, @AuthUserId String studentInfo) {
        service.removeOutGo(outGoIdx, studentInfo);
    }

    @PutMapping("/{idx}/allow")
    public void allowOutGo(@PathVariable("idx") int outGoIdx, @AuthUserId String uuid) {
        service.allowOutGO(outGoIdx, uuid);
    }

    @PutMapping("/{idx}/use")
    public void useOutGo(@PathVariable("idx") int outGoIdx, @AuthUserId String uuid) {
        service.useOutGo(outGoIdx, uuid);
    }

    @PutMapping("/{idx}/unuse")
    public void unuseOutGo(@PathVariable("idx") int outGoIdx, @AuthUserId String uuid) {
        service.unuseOutGo(outGoIdx, uuid);
    }
}
