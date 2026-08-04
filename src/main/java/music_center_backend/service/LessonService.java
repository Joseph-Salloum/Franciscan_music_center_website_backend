package music_center_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import music_center_backend.exception.exceptions.LessonNotFoundException;
import music_center_backend.exception.exceptions.UserNotFoundException;
import music_center_backend.model.dto.lesson.CreateLessonRequest;
import music_center_backend.model.dto.lesson.LessonResponse;
import music_center_backend.model.dto.lesson.UpdateLessonRequest;
import music_center_backend.model.entity.Lesson;
import music_center_backend.model.entity.Student;
import music_center_backend.model.entity.Teacher;
import music_center_backend.repository.LessonRepository;
import music_center_backend.repository.StudentRepository;
import music_center_backend.repository.TeacherRepository;
import music_center_backend.security.user.CurrentUserService;
import music_center_backend.util.HashGenerator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CurrentUserService currentUserService;
    
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and @authoritiesChecker.currentOwnStudent(#studentPublicId))")
    public List<LessonResponse> getLessons(String studentPublicId, LocalDate date, LocalDate startDate, LocalDate endDate) {
        return toResponse(lessonRepository.getLessons(studentPublicId, date, startDate, endDate));
    }
    public List<LessonResponse> getMyLessons(LocalDate date, LocalDate startDate, LocalDate endDate) {
        String studentPublicId = currentUserService.getPublicId();
        return toResponse(lessonRepository.getLessons(studentPublicId, date, startDate, endDate));
    }
    public List<LessonResponse> getByTeacherPublicId(String teacherPublicId) {
        return toResponse(lessonRepository.findByTeacher_PublicId(teacherPublicId));
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER') and @authoritiesChecker.currentOwnStudent(#studentPublicId)")
    public LessonResponse createLesson(String studentPublicId, CreateLessonRequest request) {
        Student student = studentRepository.findByPublicId(studentPublicId)
                .orElseThrow(() -> new UserNotFoundException("Student with public ID " + studentPublicId + " not found"));
        Teacher teacher = teacherRepository.findByPublicId(currentUserService.getPublicId())
                .orElseThrow(() -> new UserNotFoundException("Teacher with public ID " + currentUserService.getPublicId() + " not found"));
        
        String publicId = createPublicId();
        Lesson lesson = mapFromCreateRequest(publicId, request, student, teacher);

        return toResponse(lessonRepository.save(lesson));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and @authoritiesChecker.canAccessStudentLesson(#lessonPublicId, #studentPublicId))")
    public LessonResponse updateLesson(String lessonPublicId, String studentPublicId, UpdateLessonRequest request) {
        Lesson lesson = lessonRepository.findByPublicId(lessonPublicId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with public ID " + lessonPublicId + " not found"));
        
        if (request.getMark() != null) {
            lesson.setMark(request.getMark());
        }
        if (request.getState() != null) {
            lesson.setState(request.getState());
        }
        if (request.getNote() != null) {
            lesson.setNote(request.getNote());
        }

        return toResponse(lesson);
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER') and @authoritiesChecker.canAccessStudentLesson(#lessonPublicId, #studentPublicId)")
    public void deleteLesson(String lessonPublicId, String studentPublicId) {
        Lesson lesson = lessonRepository.findByPublicId(lessonPublicId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with public ID " + lessonPublicId + " not found"));
            
        lessonRepository.deleteById(lesson.getId());
    }

    private Lesson mapFromCreateRequest(String publicId, CreateLessonRequest request, Student student, Teacher teacher) {
        boolean instrument = !teacher.getSpecialization().name().equalsIgnoreCase("SOLFEIGE");
        return new Lesson(publicId, request.getDate(), student, teacher, request.getState(), request.getMark(), request.getNote(), instrument);
    }
    private LessonResponse toResponse(Lesson lesson) {
        return new LessonResponse(
                            lesson.getPublicId(),
                            lesson.getDate(),
                            lesson.getState().toString(),
                            lesson.getMark(),
                            lesson.getNote()
                    );
    }
    private List<LessonResponse> toResponse(List<Lesson> lessons) {
        return lessons.stream().map(this::toResponse).toList();
    }
    private String createPublicId() {
        return HashGenerator.generateRandomHash(8);
    }
}
