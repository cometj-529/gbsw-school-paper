package kr.hs.gbsw.schoolpaper.user.service;

import kr.hs.gbsw.schoolpaper.user.domain.RoleEntity;
import kr.hs.gbsw.schoolpaper.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    public RoleEntity findById(String id) {
        RoleEntity role = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할"));

        return role;
    }
}
