package kr.hs.gbsw.schoolpaper.events.controller;

import kr.hs.gbsw.schoolpaper.events.domain.EventTimeEntity;
import kr.hs.gbsw.schoolpaper.events.service.EventTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
@ResponseBody
@RequiredArgsConstructor
public class EventController {
    private final EventTimeService service;

    @GetMapping("")
    public List<EventTimeEntity> getAll() {
        return service.getAll();
    }
}
