package kr.hs.gbsw.schoolpaper.user.repository;

import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import kr.hs.gbsw.schoolpaper.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Boolean existsByTel(String tel);

    Boolean existsByStudent(StudentEntity student);

    Optional<UserEntity> findByStudent(StudentEntity student);

    Optional<UserEntity> findByTel(String tel);
}
