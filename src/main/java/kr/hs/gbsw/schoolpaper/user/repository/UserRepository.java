package kr.hs.gbsw.schoolpaper.user.repository;

import kr.hs.gbsw.schoolpaper.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByTel(String tel);
    Optional<UserEntity> findByTel(String tel);
}
