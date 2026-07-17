package music_center_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import music_center_backend.exception.exceptions.MedalNotFoundException;
import music_center_backend.exception.exceptions.UserNotFoundException;
import music_center_backend.model.dto.studentmedal.AssignStudentMedalRequest;
import music_center_backend.model.dto.studentmedal.StudentMedalResponse;
import music_center_backend.model.entity.Medal;
import music_center_backend.model.entity.Student;
import music_center_backend.model.entity.StudentMedal;
import music_center_backend.repository.MedalRepository;
import music_center_backend.repository.StudentMedalRepository;
import music_center_backend.repository.StudentRepository;

@Service
@Transactional(readOnly = true)
public class StudentMedalService {
    private final StudentMedalRepository studentMedalRepository;
    private final StudentRepository studentRepository;
    private final MedalRepository medalRepository;

    public StudentMedalService(StudentMedalRepository studentMedalRepository, StudentRepository studentRepository, MedalRepository medalRepository) {
        this.studentMedalRepository = studentMedalRepository;
        this.studentRepository = studentRepository;
        this.medalRepository = medalRepository;
    }

    @PreAuthorize("hasRole('ADMIN")
    public List<StudentMedalResponse> getAll() {
        return toResponse(studentMedalRepository.findAll());
    }
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and @authoritiesChecker.currentOwnStudent(#studentPublicId))")
    public List<StudentMedalResponse> searchMedals(String studentPublicId, LocalDate startDate, LocalDate endDate) {
        return toResponse(studentMedalRepository.searchStudentMedals(studentPublicId, startDate, endDate));
    }
    public List<StudentMedalResponse> getByMedalName(String medalName) {
        return toResponse(studentMedalRepository.findByMedal_MedalName(medalName));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and @authoritiesChecker.currentOwnStudent(#studentPublicId))")
    public StudentMedalResponse assign(String studentPublicId, AssignStudentMedalRequest request) {
        StudentMedal studentMedal = mapFromAssignRequest(studentPublicId, request);

        return toResponse(studentMedalRepository.save(studentMedal));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and @authoritiesChecker.currentOwnStudent(#studentPublicId))")
    public void remove(String studentPublicId, String medalName, LocalDate medalDate) {
        StudentMedal studentMedal = studentMedalRepository.findByStudent_PublicIdAndMedal_MedalNameAndMedalDate(studentPublicId, medalName, medalDate)
                            .orElseThrow(() -> new MedalNotFoundException("No " + medalName + " assigned to " + studentPublicId + " at " + medalDate));

        studentMedalRepository.deleteById(studentMedal.getId());
    }

    private StudentMedal mapFromAssignRequest(String studentPublicId, AssignStudentMedalRequest request) {
        Student student = studentRepository.findByPublicId(studentPublicId)
                            .orElseThrow(() -> new UserNotFoundException("No student with id " + studentPublicId));
        Medal medal = medalRepository.findByMedalName(request.getMedalName())
                            .orElseThrow(() -> new MedalNotFoundException("No medal with name " + request.getMedalName()));
        
        StudentMedal studentMedal = new StudentMedal(student, medal, request.getMedalDate());
        return studentMedal;
    }
    private StudentMedalResponse toResponse(StudentMedal studentMedal) {
        return new StudentMedalResponse(
                        studentMedal.getStudent().getPublicId(),
                        studentMedal.getMedal().getMedalName(),
                        studentMedal.getMedalDate()
                    );
    }
    private List<StudentMedalResponse> toResponse(List<StudentMedal> studentMedals) {
        return studentMedals.stream().map(this::toResponse).toList();
    }
}
