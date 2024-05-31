package kr.hs.gbsw.schoolpaper.user.repository;

import kr.hs.gbsw.schoolpaper.user.domain.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {
}
