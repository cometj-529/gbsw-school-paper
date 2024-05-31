package kr.hs.gbsw.schoolpaper.user.controller;

import kr.hs.gbsw.schoolpaper.user.dto.UserLoginDto;
import kr.hs.gbsw.schoolpaper.user.dto.UserRegisterDto;
import kr.hs.gbsw.schoolpaper.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@ResponseBody
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto dto) {
        return service.login(dto);
    }

    @PostMapping("/register")
    public void register(@RequestBody UserRegisterDto dto) {
        service.register(dto);
    }

    @PostMapping("/register/admin")
    public void adminRegister(@RequestBody UserTeacherRegisterDto dto) {
        service.adminRegister(dto);
    }
}
