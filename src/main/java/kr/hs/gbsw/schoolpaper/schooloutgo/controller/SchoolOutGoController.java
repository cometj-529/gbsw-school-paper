package kr.hs.gbsw.schoolpaper.schooloutgo.controller;

import kr.hs.gbsw.schoolpaper.common.AuthUserId;
import kr.hs.gbsw.schoolpaper.schooloutgo.domain.SchoolOutGoEntity;
import kr.hs.gbsw.schoolpaper.schooloutgo.dto.SchoolOutGoCreateDto;
import kr.hs.gbsw.schoolpaper.schooloutgo.dto.SchoolOutGoInQrDto;
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
    public List<SchoolOutGoEntity> getByClassInfo(@PathVariable("grade") int grade, @PathVariable("classNumber") int classNumber, @AuthUserId String uuid) {

        List<SchoolOutGoEntity> results = service.getByClassInfo(grade, classNumber, uuid);

        return results;
    }

    @GetMapping("/{idx}")
    public SchoolOutGoInQrDto getById(@PathVariable("idx") int idx, @AuthUserId String uuid) throws Exception {
        return service.findByIdx(idx, uuid);
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

    @PutMapping("/use")
    public void useOutGo(@RequestParam("token") String token, @AuthUserId String uuid) {
        service.useOutGo(token, uuid);
    }

    @PutMapping("/{idx}/unuse")
    public void unuseOutGo(@PathVariable("idx") int outGoIdx, @AuthUserId String uuid) {
        service.unuseOutGo(outGoIdx, uuid);
    }
}
