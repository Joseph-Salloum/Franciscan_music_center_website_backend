package music_center_backend.service;

import java.util.List;

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

    public List<StudentMedalResponse> getAll() {
        return toResponse(studentMedalRepository.findAll());
    }
    public List<StudentMedalResponse> getByStudentPublicId(String studentPublicId) {
        return toResponse(studentMedalRepository.findByStudentPublicId(studentPublicId));
    }
    public List<StudentMedalResponse> getByMedalName(String medalName) {
        return toResponse(studentMedalRepository.findByMedalMedalName(medalName));
    }

    private StudentMedal mapFromAssignRequest(AssignStudentMedalRequest request) {
        Student student = studentRepository.findByPublicId(request.getStudentPublicId())
                            .orElseThrow(() -> new UserNotFoundException("No student with id " + request.getStudentPublicId()));
        Medal medal = medalRepository.findByMedalName(request.getMedalName())
                            .orElseThrow(() -> new MedalNotFoundException("No medal with name " + request.getMedalName()));
        
        StudentMedal studentMedal = new StudentMedal(student, medal, null);
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
