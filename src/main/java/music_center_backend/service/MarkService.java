package music_center_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import music_center_backend.exception.exceptions.IllegalOperationException;
import music_center_backend.exception.exceptions.UserNotFoundException;
import music_center_backend.model.dto.mark.CreateMarkRequest;
import music_center_backend.model.dto.mark.MarkResponse;
import music_center_backend.model.dto.mark.UpdateMarkRequest;
import music_center_backend.model.entity.Mark;
import music_center_backend.model.entity.Student;
import music_center_backend.model.entity.Teacher;
import music_center_backend.repository.MarkRepository;
import music_center_backend.repository.StudentRepository;
import music_center_backend.repository.TeacherRepository;
import music_center_backend.util.HashGenerator;

@Service
@Transactional(readOnly = true)
public class MarkService {
    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public MarkService(MarkRepository markRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.markRepository = markRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<MarkResponse> getMarks(String studentPublicId, LocalDate date, LocalDate startDate, LocalDate endDate) {
        return toResponse(markRepository.getMarks(studentPublicId, date, startDate, endDate));
    }
    public List<MarkResponse> getByTeacherPublicId(String teacherPublicId) {
        return toResponse(markRepository.findByTeacherPublicId(teacherPublicId));
    }

    @Transactional
    public MarkResponse createMark(String teacherPublicId, String studentPublicId, CreateMarkRequest request) {
        Student student = studentRepository.findByPublicId(studentPublicId)
                .orElseThrow(() -> new UserNotFoundException("Student with public ID " + studentPublicId + " not found"));
        Teacher teacher = teacherRepository.findByPublicId(teacherPublicId)
                .orElseThrow(() -> new UserNotFoundException("Teacher with public ID " + teacherPublicId + " not found"));
        
        String publicId = createPublicId(request);
        Mark mark = mapFromCreateRequest(publicId, request, student, teacher);
        
        return toResponse(markRepository.save(mark));
    }

    @Transactional
    public short updateMark(String publicId, String teacherPublicId, String studentPublicId, UpdateMarkRequest request) {
        Mark mark = markRepository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("No mark with public ID " + publicId));
        Teacher teacher = teacherRepository.findByPublicId(teacherPublicId)
                .orElseThrow(() -> new UserNotFoundException("Teacher with public ID " + teacherPublicId + " not found"));
        
        if (!teacher.isAdmin() && (!mark.getStudent().getPublicId().equals(studentPublicId) || !mark.getTeacher().getPublicId().equals(teacherPublicId))) {
            throw new IllegalOperationException("You are not the owner of this mark");
        }

        mark.setMark(request.getMark());
        return mark.getMark();
    }

    @Transactional
    public void deleteMark(String publicId, String teacherPublicId, String studentPublicId) {
        Mark mark = markRepository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("No mark with public ID " + publicId));

        if (!mark.getStudent().getPublicId().equals(studentPublicId) && !mark.getTeacher().getPublicId().equals(teacherPublicId)) {
            throw new IllegalArgumentException("Mark with public ID " + publicId + " does not belong to the specified student and teacher");
        }

        markRepository.deleteById(mark.getId());
    }

    private Mark mapFromCreateRequest(String publicId, CreateMarkRequest request, Student student, Teacher teacher) {
        Mark mark = new Mark(publicId, student, teacher, request.getMark(), request.getDate(), request.isInstrument());
        return mark;
    }
    private MarkResponse toResponse(Mark mark) {
        return new MarkResponse(
                        mark.getPublicId(),
                        mark.getMark(),
                        mark.getDate(),
                        mark.isInstrument()
                    );
    }
    private List<MarkResponse> toResponse(List<Mark> marks) {
        return marks.stream().map(this::toResponse).toList();
    }
    private String createPublicId(CreateMarkRequest request) {
        return HashGenerator.generateRandomHash(8);
    }
}
