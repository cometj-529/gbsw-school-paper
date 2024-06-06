package kr.hs.gbsw.schoolpaper.events.service;

import kr.hs.gbsw.schoolpaper.events.domain.EventTimeEntity;
import kr.hs.gbsw.schoolpaper.events.repository.EventTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventTimeService {
    private final EventTimeRepository repository;

    public List<EventTimeEntity> getAll() {
        return repository.findAll();
    }

    public EventTimeEntity findById(String id) {
        EventTimeEntity eventTime = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트 시간"));

        return eventTime;
    }
}
