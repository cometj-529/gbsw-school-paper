package kr.hs.gbsw.schoolpaper.user.controller;

import kr.hs.gbsw.schoolpaper.common.AuthUserId;
import kr.hs.gbsw.schoolpaper.user.domain.UserEntity;
import kr.hs.gbsw.schoolpaper.user.dto.UserLoginDto;
import kr.hs.gbsw.schoolpaper.user.dto.UserStudentRegisterDto;
import kr.hs.gbsw.schoolpaper.user.dto.UserTeacherRegisterDto;
import kr.hs.gbsw.schoolpaper.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@ResponseBody
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/my")
    public UserEntity getMy(@AuthUserId String uuid) {
        return service.getMy(uuid);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto dto) {
        return service.login(dto);
    }

    @PostMapping("/register")
    public void register(@RequestBody UserStudentRegisterDto dto) {
        service.register(dto);
    }

    @PostMapping("/register/admin")
    public void adminRegister(@RequestBody UserTeacherRegisterDto dto) {
        service.adminRegister(dto);
    }

    @DeleteMapping("")
    public void delete(@RequestParam("uuid") String uuid) {
        service.delete(uuid);
    }
}
