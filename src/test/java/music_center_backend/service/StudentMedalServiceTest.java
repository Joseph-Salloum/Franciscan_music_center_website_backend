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

import music_center_backend.exception.exceptions.MedalNotFoundException;
import music_center_backend.model.constant.Specialization;
import music_center_backend.model.dto.studentmedal.AssignStudentMedalRequest;
import music_center_backend.model.dto.studentmedal.StudentMedalResponse;
import music_center_backend.model.entity.Medal;
import music_center_backend.model.entity.Student;
import music_center_backend.model.entity.StudentMedal;
import music_center_backend.model.entity.Teacher;
import music_center_backend.repository.MedalRepository;
import music_center_backend.repository.StudentMedalRepository;
import music_center_backend.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentMedalServiceTest {

    @Mock
    private StudentMedalRepository studentMedalRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private MedalRepository medalRepository;

    private StudentMedalService studentMedalService;

    @BeforeEach
    void setUp() {
        studentMedalService = new StudentMedalService(studentMedalRepository, studentRepository, medalRepository);
    }

    @Test
    @DisplayName("searchMedals should map repository results")
    void searchMedalsShouldMapRepositoryResults() {
        StudentMedal studentMedal = new StudentMedal(createStudent(), new Medal("Star", "Great progress"), LocalDate.of(2025, 4, 12));
        when(studentMedalRepository.searchStudentMedals("student-1", null, null)).thenReturn(List.of(studentMedal));

        List<StudentMedalResponse> responses = studentMedalService.searchMedals("student-1", null, null);

        assertEquals(1, responses.size());
        assertEquals("student-1", responses.get(0).getStudentPublicId());
        assertEquals("Star", responses.get(0).getMedalName());
    }

    @Test
    @DisplayName("assign should persist a student medal after resolving student and medal")
    void assignShouldPersistStudentMedal() {
        Student student = createStudent();
        Medal medal = new Medal("Star", "Great progress");
        AssignStudentMedalRequest request = new AssignStudentMedalRequest();
        request.setStudentPublicId("student-1");
        request.setMedalName("Star");
        request.setMedalDate(LocalDate.of(2025, 4, 12));

        when(studentRepository.findByPublicId("student-1")).thenReturn(Optional.of(student));
        when(medalRepository.findByMedalName("Star")).thenReturn(Optional.of(medal));
        when(studentMedalRepository.save(any(StudentMedal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentMedalResponse response = studentMedalService.assign(request);

        ArgumentCaptor<StudentMedal> captor = ArgumentCaptor.forClass(StudentMedal.class);
        verify(studentMedalRepository).save(captor.capture());
        assertEquals(student, captor.getValue().getStudent());
        assertEquals(medal, captor.getValue().getMedal());
        assertEquals("student-1", response.getStudentPublicId());
        assertEquals("Star", response.getMedalName());
    }

    @Test
    @DisplayName("remove should delete the matched medal assignment")
    void removeShouldDeleteMatchedAssignment() {
        StudentMedal studentMedal = new StudentMedal(createStudent(), new Medal("Star", "Great progress"), LocalDate.of(2025, 4, 12));

        when(studentMedalRepository.findByStudent_PublicIdAndMedal_MedalNameAndMedalDate("student-1", "Star", LocalDate.of(2025, 4, 12)))
                .thenReturn(Optional.of(studentMedal));

        studentMedalService.remove("student-1", "Star", LocalDate.of(2025, 4, 12));

        verify(studentMedalRepository).deleteById(studentMedal.getId());
    }

    @Test
    @DisplayName("remove should throw when the medal assignment does not exist")
    void removeShouldThrowWhenMissing() {
        when(studentMedalRepository.findByStudent_PublicIdAndMedal_MedalNameAndMedalDate("student-1", "Star", LocalDate.of(2025, 4, 12)))
                .thenReturn(Optional.empty());

        assertThrows(MedalNotFoundException.class, () -> studentMedalService.remove("student-1", "Star", LocalDate.of(2025, 4, 12)));
    }

    private Student createStudent() {
        Teacher teacher = new Teacher("teacher-1", "Ms. Lee", Specialization.PIANO, false);
        return new Student("student-1", "Mia", LocalDate.of(2024, 1, 10), "Piano", teacher, true);
    }
}