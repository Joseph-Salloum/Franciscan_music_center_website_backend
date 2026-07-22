package music_center_backend.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import music_center_backend.exception.exceptions.IllegalOperationException;
import music_center_backend.exception.exceptions.UserNotFoundException;
import music_center_backend.model.dto.profile.TeacherPublicProfileResponse;
import music_center_backend.model.dto.student.StudentResponse;
import music_center_backend.model.dto.teacher.CreateTeacherRequest;
import music_center_backend.model.dto.teacher.TeacherResponse;
import music_center_backend.model.dto.teacher.UpdateTeacherRequest;
import music_center_backend.model.entity.Teacher;
import music_center_backend.repository.TeacherRepository;
import music_center_backend.security.user.CurrentUserService;
import music_center_backend.util.HashGenerator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final CurrentUserService currentUserService;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    public List<TeacherResponse> getAllExceptCurrentTeacher() {
        String currentPublicId = currentUserService.getPublicId();

        return teacherRepository.findByPublicIdNot(currentPublicId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public TeacherResponse getMyProfile() {
        String currentUserPublicId = currentUserService.getPublicId();
        return getByPublicId(currentUserPublicId);
    }
    public List<TeacherPublicProfileResponse> getPublicProfiles() {
        return teacherRepository.findAll().stream()
                .map(teacher -> new TeacherPublicProfileResponse(
                        teacher.getName(),
                        teacher.getSpecialization().toString(),
                        teacher.isAdmin()
                ))
                .toList();
    }
    public TeacherResponse getByPublicId(String publicId) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                            .orElseThrow(() -> new UserNotFoundException("No teacher with id " + publicId));
        return toResponse(teacher);
    }
    @PreAuthorize("hasRole('TEACHER')")
    public List<StudentResponse> getMyStudents() {
        String currentUserPublicId = currentUserService.getPublicId();
        return studentService.getByTeacherPublicId(currentUserPublicId);
    }
    

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public TeacherResponse createTeacher(CreateTeacherRequest request) {
        String publicId = createPublicId(request);
        Teacher newTeacher = mapFromCreateRequest(publicId, request);
        
        String accessCodeHash = passwordEncoder.encode(request.getAccessCode());
        newTeacher.setAccessCode(accessCodeHash);

        return toResponse(teacherRepository.save(newTeacher));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public TeacherResponse updateTeacher(String publicId, UpdateTeacherRequest request) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UserNotFoundException("No teacher with id " + publicId));
        
        String currentUserPublicId = currentUserService.getPublicId();

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

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTeacher(String publicId) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UserNotFoundException("No teacher with id " + publicId));
        
        if (studentService.existsByTeacherPublicId(publicId)) {
            throw new IllegalOperationException("Cannot delete teacher because they still have assigned students");
        }

        teacherRepository.deleteById(teacher.getId());
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
