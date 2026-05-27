package music_center_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import music_center_backend.exception.exceptions.IllegalOperationException;
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
import music_center_backend.util.HashGenerator;

@Service
@Transactional(readOnly = true)
public class LessonService {
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    
    public LessonService(LessonRepository lessonRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<LessonResponse> getLessons(String studentPublicId, LocalDate date, LocalDate startDate, LocalDate endDate) {
        return toResponse(lessonRepository.getLessons(studentPublicId, date, startDate, endDate));
    }
    public List<LessonResponse> getByTeacherPublicId(String teacherPublicId) {
        return toResponse(lessonRepository.findByTeacherPublicId(teacherPublicId));
    }

    @Transactional
    public LessonResponse createLesson(String teacherPublicId, String studentPublicId, CreateLessonRequest request) {
        Student student = studentRepository.findByPublicId(studentPublicId)
                .orElseThrow(() -> new UserNotFoundException("Student with public ID " + studentPublicId + " not found"));
        Teacher teacher = teacherRepository.findByPublicId(teacherPublicId)
                .orElseThrow(() -> new UserNotFoundException("Teacher with public ID " + teacherPublicId + " not found"));
        
        String publicId = createPublicId();
        Lesson lesson = mapFromCreateRequest(publicId, request, student, teacher);

        return toResponse(lessonRepository.save(lesson));
    }

    @Transactional
    public LessonResponse updateLesson(String publicId, String teacherPublicId, String studentPublicId, UpdateLessonRequest request) {
        Lesson lesson = lessonRepository.findByPublicId(publicId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with public ID " + publicId + " not found"));
        Teacher teacher = teacherRepository.findByPublicId(teacherPublicId)
                .orElseThrow(() -> new UserNotFoundException("Teacher with public ID " + teacherPublicId + " not found"));
        
        if (!teacher.isAdmin() && (!lesson.getTeacher().getPublicId().equals(teacherPublicId) || !lesson.getStudent().getPublicId().equals(studentPublicId))) {
            throw new IllegalOperationException("Lesson with public ID " + publicId + " does not belong to the specified student and teacher");
        }

        lesson.setMark(request.getMark());
        lesson.setState(request.getState());
        lesson.setNote(request.getNote());

        return toResponse(lesson);
    }

    @Transactional
    public void deleteLesson(String publicId, String teacherPublicId, String studentPublicId) {
        Lesson lesson = lessonRepository.findByPublicId(publicId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with public ID " + publicId + " not found"));
            
        if (!lesson.getTeacher().getPublicId().equals(teacherPublicId) || !lesson.getStudent().getPublicId().equals(studentPublicId)) {
            throw new IllegalOperationException("Lesson with public ID " + publicId + " does not belong to the specified student and teacher");
        }

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
