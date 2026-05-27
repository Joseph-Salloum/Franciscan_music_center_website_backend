package music_center_backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import music_center_backend.exception.exceptions.UserNotFoundException;
import music_center_backend.model.dto.teacher.CreateTeacherRequest;
import music_center_backend.model.dto.teacher.TeacherResponse;
import music_center_backend.model.dto.teacher.UpdateTeacherRequest;
import music_center_backend.model.entity.Teacher;
import music_center_backend.repository.TeacherRepository;
import music_center_backend.security.CurrentUserProvider;
import music_center_backend.util.HashGenerator;

@Service
@Transactional(readOnly = true)
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final CurrentUserProvider currentUserProvider;
    private final PasswordEncoder passwordEncoder;

    public TeacherService(TeacherRepository teacherRepository, CurrentUserProvider currentUserProvider, PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.currentUserProvider = currentUserProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public List<TeacherResponse> getAll() {
        return toResponse(teacherRepository.findAll());
    }
    public TeacherResponse getByPublicId(String publicId) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                            .orElseThrow(() -> new UserNotFoundException("No teacher with id " + publicId));
        return toResponse(teacher);
    }

    @Transactional
    public TeacherResponse createTeacher(CreateTeacherRequest request) {
        String publicId = createPublicId(request);
        Teacher newTeacher = mapFromCreateRequest(publicId, request);
        
        String accessCodeHash = passwordEncoder.encode(request.getAccessCode());
        newTeacher.setAccessCode(accessCodeHash);

        return toResponse(teacherRepository.save(newTeacher));
    }

    @Transactional
    public TeacherResponse updateTeacher(String publicId, UpdateTeacherRequest request) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UserNotFoundException("No teacher with id " + publicId));
        
        String currentUserPublicId = currentUserProvider.getCurrentUserPublicId();

        if (request.getAdmin() != null
            && !request.getAdmin()
            && currentUserPublicId.equals(publicId)) {

            throw new IllegalStateException("Cannot change your own role");
        }

        if (request.getName() != null) {
            teacher.setName(request.getName());
        }
        if (request.getSpecialization() != null) {
            teacher.setSpecialization(request.getSpecialization());
        }
        if (request.getAdmin() != null) {
            teacher.setAdmin(request.getAdmin());
        }

        return toResponse(teacher);
    }

    private Teacher mapFromCreateRequest(String publicId, CreateTeacherRequest request) {
        Teacher teacher = new Teacher(publicId, request.getName(), request.getSpecialization(), request.isAdmin());
        return teacher;
    }
    private TeacherResponse toResponse(Teacher teacher) {
        return new TeacherResponse(teacher.getPublicId(),
                        teacher.getName(),
                        teacher.getSpecialization().toString(),
                        teacher.isAdmin()
                    );
    }
    private List<TeacherResponse> toResponse(List<Teacher> teachers) {
        return teachers.stream().map(this::toResponse).toList();
    }
    private String createPublicId(CreateTeacherRequest request) {
        StringBuilder publicIdBuilder = new StringBuilder();
        publicIdBuilder
                    .append(request.getName())
                    .append("-")
                    .append(HashGenerator.generateRandomHash(8))
                    .append("-t");
        return publicIdBuilder.toString();
    }
}
