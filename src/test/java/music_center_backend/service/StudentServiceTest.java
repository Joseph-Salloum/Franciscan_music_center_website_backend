package music_center_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import music_center_backend.exception.exceptions.IllegalOperationException;
import music_center_backend.model.constant.Specialization;
import music_center_backend.model.dto.student.CreateStudentRequest;
import music_center_backend.model.dto.student.StudentResponse;
import music_center_backend.model.dto.student.UpdateStudentRequest;
import music_center_backend.model.entity.Student;
import music_center_backend.model.entity.Teacher;
import music_center_backend.repository.StudentRepository;
import music_center_backend.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, teacherRepository, passwordEncoder);
    }

    @Test
    @DisplayName("getByTeacherPublicId should map the teacher's students")
    void getByTeacherPublicIdShouldMapStudents() {
        Teacher teacher = new Teacher("teacher-1", "Ms. Lee", Specialization.PIANO, false);
        Student student = new Student("student-1", "Mia", LocalDate.of(2024, 1, 10), "Piano", teacher, true);

        when(teacherRepository.findByPublicId("teacher-1")).thenReturn(Optional.of(teacher));
        when(studentRepository.findByTeacherPublicId("teacher-1")).thenReturn(List.of(student));

        List<StudentResponse> responses = studentService.getByTeacherPublicId("teacher-1");

        assertEquals(1, responses.size());
        assertEquals("student-1", responses.get(0).getPubilcId());
        assertEquals("Ms. Lee", responses.get(0).getTeacherName());
    }

    @Test
    @DisplayName("createStudent should create and persist a student with an encoded access code")
    void createStudentShouldCreateAndPersistStudent() {
        Teacher teacher = new Teacher("teacher-1", "Ms. Lee", Specialization.PIANO, false);
        CreateStudentRequest request = new CreateStudentRequest();
        request.setName("Mia");
        request.setDateOfStart(LocalDate.of(2024, 1, 10));
        request.setInstrument("Piano");
        request.setTeacherPublicId("teacher-1");
        request.setTakingSolfeige(true);
        request.setAccessCode("1234");

        when(teacherRepository.findByPublicId("teacher-1")).thenReturn(Optional.of(teacher));
        when(passwordEncoder.encode("1234")).thenReturn("hashed-access-code");
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentResponse response = studentService.createStudent(request);

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentCaptor.capture());
        assertEquals("Mia", studentCaptor.getValue().getName());
        assertEquals("hashed-access-code", studentCaptor.getValue().getAccessCode());
        assertEquals("Mia", response.getName());
        assertEquals("Ms. Lee", response.getTeacherName());
    }

    @Test
    @DisplayName("updateStudent should update fields when the teacher owns the student")
    void updateStudentShouldUpdateOwnedStudent() {
        Teacher teacher = new Teacher("teacher-1", "Ms. Lee", Specialization.PIANO, false);
        Student student = new Student("student-1", "Mia", LocalDate.of(2024, 1, 10), "Piano", teacher, true);
        UpdateStudentRequest request = new UpdateStudentRequest();
        request.setName("Mia Updated");
        request.setInstrument("Violin");
        request.setTakingSolfeige(false);

        when(studentRepository.findByPublicId("student-1")).thenReturn(Optional.of(student));
        when(teacherRepository.findByPublicId("teacher-1")).thenReturn(Optional.of(teacher));

        StudentResponse response = studentService.updateStudent("student-1", "teacher-1", request);

        assertEquals("Mia Updated", response.getName());
        assertEquals("Violin", response.getInstrument());
        assertFalse(response.isTakingSolfeige());
    }

    @Test
    @DisplayName("updateStudent should reject a non-admin teacher who does not own the student")
    void updateStudentShouldRejectNonOwnerTeacher() {
        Teacher owner = new Teacher("teacher-1", "Ms. Lee", Specialization.PIANO, false);
        Teacher otherTeacher = new Teacher("teacher-2", "Mr. Kim", Specialization.GUITAR, false);
        Student student = new Student("student-1", "Mia", LocalDate.of(2024, 1, 10), "Piano", owner, true);
        UpdateStudentRequest request = new UpdateStudentRequest();

        when(studentRepository.findByPublicId("student-1")).thenReturn(Optional.of(student));
        when(teacherRepository.findByPublicId("teacher-2")).thenReturn(Optional.of(otherTeacher));

        assertThrows(IllegalOperationException.class, () -> studentService.updateStudent("student-1", "teacher-2", request));
    }

    @Test
    @DisplayName("deleteStudent should delete the looked-up student")
    void deleteStudentShouldDeleteLookedUpStudent() {
        Teacher teacher = new Teacher("teacher-1", "Ms. Lee", Specialization.PIANO, false);
        Student student = new Student("student-1", "Mia", LocalDate.of(2024, 1, 10), "Piano", teacher, true);

        when(studentRepository.findByPublicId("student-1")).thenReturn(Optional.of(student));

        studentService.deleteStudent("student-1");

        verify(studentRepository).deleteById(student.getId());
    }
}