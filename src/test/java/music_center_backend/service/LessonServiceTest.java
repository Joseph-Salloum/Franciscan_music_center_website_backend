package music_center_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import music_center_backend.exception.exceptions.LessonNotFoundException;
import music_center_backend.model.constant.Specialization;
import music_center_backend.model.constant.State;
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

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private CurrentUserService currentUserService;

    private LessonService lessonService;

    @BeforeEach
    void setUp() {
        lessonService = new LessonService(lessonRepository, studentRepository, teacherRepository, currentUserService);
    }

    @Test
    @DisplayName("getLessons should map the repository result")
    void getLessonsShouldMapRepositoryResult() {
        Lesson lesson = new Lesson("lesson-1", LocalDate.of(2025, 3, 5), createStudent(), createTeacher(false), State.PRESENT, (short) 9, "Great lesson", true);
        when(lessonRepository.getLessons("student-1", null, null, null)).thenReturn(List.of(lesson));

        List<LessonResponse> responses = lessonService.getLessons("student-1", null, null, null);

        assertEquals(1, responses.size());
        assertEquals("lesson-1", responses.get(0).getPublicId());
        assertEquals("PRESENT", responses.get(0).getState());
    }

    @Test
    @DisplayName("createLesson should persist a lesson for the matched student and teacher")
    void createLessonShouldPersistLesson() {
        Student student = createStudent();
        Teacher teacher = createTeacher(false);
        CreateLessonRequest request = new CreateLessonRequest();
        request.setDate(LocalDate.of(2025, 3, 5));
        request.setState(State.EXCUSED);
        request.setMark((short) 7);
        request.setNote("Needs work");

        when(studentRepository.findByPublicId("student-1")).thenReturn(Optional.of(student));
        when(currentUserService.getPublicId()).thenReturn("teacher-1");
        when(teacherRepository.findByPublicId("teacher-1")).thenReturn(Optional.of(teacher));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LessonResponse response = lessonService.createLesson("student-1", request);

        ArgumentCaptor<Lesson> lessonCaptor = ArgumentCaptor.forClass(Lesson.class);
        verify(lessonRepository).save(lessonCaptor.capture());
        assertEquals(student, lessonCaptor.getValue().getStudent());
        assertEquals(teacher, lessonCaptor.getValue().getTeacher());
        assertEquals("EXCUSED", response.getState());
        assertEquals(7, response.getMark());
    }

    @Test
    @DisplayName("updateLesson should update provided fields")
    void updateLessonShouldUpdateProvidedFields() {
        Student student = createStudent();
        Teacher owner = createTeacher(false);
        Lesson lesson = new Lesson("lesson-1", LocalDate.of(2025, 3, 5), student, owner, State.ABSENT, (short) 4, "Old note", true);
        UpdateLessonRequest request = new UpdateLessonRequest();
        request.setState(State.PRESENT);
        request.setMark((short) 10);
        request.setNote("Improved");

        when(lessonRepository.findByPublicId("lesson-1")).thenReturn(Optional.of(lesson));

        LessonResponse response = lessonService.updateLesson("lesson-1", "student-1", request);

        assertEquals("PRESENT", response.getState());
        assertEquals(10, response.getMark());
        assertEquals("Improved", response.getNote());
    }

    @Test
    @DisplayName("updateLesson should throw when lesson does not exist")
    void updateLessonShouldThrowWhenLessonMissing() {
        Student student = createStudent();
        Teacher owner = createTeacher(false);
        @SuppressWarnings("unused")
        Lesson lesson = new Lesson("lesson-1", LocalDate.of(2025, 3, 5), student, owner, State.ABSENT, (short) 4, "Old note", true);
        UpdateLessonRequest request = new UpdateLessonRequest();

        when(lessonRepository.findByPublicId("lesson-1")).thenReturn(Optional.empty());

        assertThrows(LessonNotFoundException.class, () -> lessonService.updateLesson("lesson-1", "student-1", request));
    }

    @Test
    @DisplayName("deleteLesson should delete the matched lesson")
    void deleteLessonShouldDeleteMatchedLesson() {
        Student student = createStudent();
        Teacher teacher = createTeacher(false);
        Lesson lesson = new Lesson("lesson-1", LocalDate.of(2025, 3, 5), student, teacher, State.ABSENT, (short) 4, "Old note", true);

        when(lessonRepository.findByPublicId("lesson-1")).thenReturn(Optional.of(lesson));

        lessonService.deleteLesson("lesson-1", "student-1");

        verify(lessonRepository).deleteById(lesson.getId());
    }

    private Student createStudent() {
        return new Student("student-1", "Mia", LocalDate.of(2024, 1, 10), "Piano", createTeacher(false), true);
    }

    private Teacher createTeacher(boolean admin) {
        return new Teacher("teacher-1", "Ms. Lee", Specialization.PIANO, admin);
    }
}