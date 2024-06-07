package kr.hs.gbsw.schoolpaper.schooloutgo.service;

import kr.hs.gbsw.schoolpaper.events.domain.EventTimeEntity;
import kr.hs.gbsw.schoolpaper.events.service.EventTimeService;
import kr.hs.gbsw.schoolpaper.jwt.JwtUtils;
import kr.hs.gbsw.schoolpaper.qr.service.QrService;
import kr.hs.gbsw.schoolpaper.schooloutgo.domain.SchoolOutGoEntity;
import kr.hs.gbsw.schoolpaper.schooloutgo.dto.SchoolOutGoCreateDto;
import kr.hs.gbsw.schoolpaper.schooloutgo.dto.SchoolOutGoInQrDto;
import kr.hs.gbsw.schoolpaper.schooloutgo.repository.SchoolOutGoRepository;
import kr.hs.gbsw.schoolpaper.student.domain.StudentEntity;
import kr.hs.gbsw.schoolpaper.user.domain.RoleEntity;
import kr.hs.gbsw.schoolpaper.user.domain.UserEntity;
import kr.hs.gbsw.schoolpaper.user.service.RoleService;
import kr.hs.gbsw.schoolpaper.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SchoolOutGoService {
    private final SchoolOutGoRepository repository;
    private final RoleService roleService;
    private final UserService userService;
    private final EventTimeService eventTimeService;
    private final JwtUtils jwtUtils;
    private final QrService qrService;

    public SchoolOutGoEntity findByIdx(int id) {
        SchoolOutGoEntity outGo = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 외출증"));

        return outGo;
    }

    public SchoolOutGoInQrDto findByIdx(int id, String uuid) throws Exception {
        SchoolOutGoEntity outGo = findByIdx(id);

        UserEntity user = userService.findByUUID(UUID.fromString(uuid));

        if (outGo.getStudent() != user.getStudent()) {
            throw new IllegalStateException("자신의 외출증만 불러올 수 있습니다.");
        }

        String qrToken = createJwt(id);
        BufferedImage qrImage = qrService.generateQR(qrToken);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);

        String qrBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());

        SchoolOutGoInQrDto dto = new SchoolOutGoInQrDto();
        dto.setIdx(outGo.getIdx());
        dto.setEventTime(outGo.getEventTime());
        dto.setUseDate(outGo.getUseDate());
        dto.setUsedChangeDateTime(outGo.getUsedChangeDateTime());
        dto.setStudent(outGo.getStudent());
        dto.setAllow(outGo.isAllow());
        dto.setUsed(outGo.isUsed());
        dto.setExpired(outGo.isExpired());
        dto.setAvailable(outGo.isAvailable());
        dto.setQrCode(qrBase64);

        return dto;
    }

    public List<SchoolOutGoEntity> getByClassInfo(int grade, int classNumber, String uuid) {

        UserEntity user = userService.findByUUID(UUID.fromString(uuid));

        RoleEntity role = roleService.findById("ADMIN");

        if (!user.getRoles().contains(role)) {
            throw new IllegalStateException("관리자가 아닙니다.");
        }

        return repository.findAllByStudentGradeAndStudentClassNumber(grade, classNumber);
    }

    public void createOutGo(SchoolOutGoCreateDto dto, String uuid) {
        if (uuid == null) throw new IllegalArgumentException("로그인 필요");

        UserEntity user = userService.findByUUID(UUID.fromString(uuid));

        StudentEntity student = user.getStudent();

        EventTimeEntity eventTime = eventTimeService.findById(dto.getEventTimeId());

        SchoolOutGoEntity entity = new SchoolOutGoEntity();
        entity.setStudent(student);
        entity.setEventTime(eventTime);
        entity.setAllow(false);
        entity.setUseDate(dto.getUseDate());
        entity.setUsed(false);

        repository.save(entity);
    }

    public List<SchoolOutGoEntity> getMy(String uuid) {

        UserEntity user = userService.findByUUID(UUID.fromString(uuid));

        StudentEntity student = user.getStudent();

        return repository.findByStudent(student);
    }

    public void removeOutGo(int outGoIdx, String uuid) {

        SchoolOutGoEntity outGo = this.findByIdx(outGoIdx);

        UserEntity user = userService.findByUUID(UUID.fromString(uuid));

        StudentEntity student = user.getStudent();

        RoleEntity role = roleService.findById("ADMIN");

        if (outGo.getStudent() != student && !user.getRoles().contains(role)) {
            throw new IllegalStateException("자신의 외출증만 취소할 수 있습니다.");
        }

        repository.delete(outGo);
    }

    public void allowOutGO(int outGoIdx, String uuid) {
        UserEntity user = userService.findByUUID(UUID.fromString(uuid));

        RoleEntity role = roleService.findById("ADMIN");

        if (!user.getRoles().contains(role)) {
            throw new IllegalStateException("관리자가 아닙니다.");
        }

        SchoolOutGoEntity outGo = this.findByIdx(outGoIdx);

        outGo.setAllow(true);

        repository.save(outGo);
    }

    public void useOutGo(String token, String uuid) {

        UserEntity user = userService.findByUUID(UUID.fromString(uuid));

        RoleEntity role = roleService.findById("ADMIN");

        if (!user.getRoles().contains(role)) {
            throw new IllegalStateException("관리자가 아닙니다.");
        }

        if (!validateOutGo(token)) {
            throw new IllegalStateException("부적절한 토큰입니다.");
        }

        int outGoIdx = Integer.parseInt(jwtUtils.getSubject(token));

        SchoolOutGoEntity outGo = this.findByIdx(outGoIdx);

        if (outGo.isUsed()) {
            throw new IllegalStateException("이미 사용된 외출증");
        }

        if (!outGo.isAllow()) {
            throw new IllegalStateException("허락되지 않은 외출증");
        }

        if (outGo.isExpired()) {
            throw new IllegalStateException("만료된 외출증");
        }

        if (!outGo.isAvailable()) {
            throw new IllegalStateException("사용가능한 시간이 아닙니다");
        }

        outGo.setUsed(true);
        outGo.setUsedChangeDateTime(LocalDateTime.now());

        repository.save(outGo);
    }

    public void unuseOutGo(int outGoIdx, String uuid) {

        UserEntity user = userService.findByUUID(UUID.fromString(uuid));

        RoleEntity role = roleService.findById("ADMIN");

        if (!user.getRoles().contains(role)) {
            throw new IllegalStateException("관리자가 아닙니다.");
        }

        SchoolOutGoEntity outGo = this.findByIdx(outGoIdx);

        if (!outGo.isUsed()) {
            throw new IllegalStateException("사용되지 않은 외출증");
        }

        if (!outGo.isAllow()) {
            throw new IllegalStateException("허락되지 않은 외출증");
        }

        if (outGo.isExpired()) {
            throw new IllegalStateException("만료된 외출증");
        }

        outGo.setUsed(false);
        outGo.setUsedChangeDateTime(LocalDateTime.now());

        repository.save(outGo);
    }

    public boolean validateOutGo(String token) {
        return jwtUtils.validate(token);
    }

    public String createJwt(int outGoIdx) {
        return jwtUtils.createOutGoJwt(outGoIdx);
    }
}
