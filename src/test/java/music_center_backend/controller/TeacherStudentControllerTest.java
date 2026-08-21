package music_center_backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import music_center_backend.model.constant.State;
import music_center_backend.model.dto.lesson.CreateLessonRequest;
import music_center_backend.model.dto.lesson.LessonResponse;
import music_center_backend.model.dto.lesson.UpdateLessonRequest;
import music_center_backend.model.dto.student.CreateStudentRequest;
import music_center_backend.model.dto.student.StudentResponse;
import music_center_backend.model.dto.student.UpdateStudentRequest;
import music_center_backend.model.dto.studentmedal.AssignStudentMedalRequest;
import music_center_backend.model.dto.studentmedal.StudentMedalResponse;
import music_center_backend.service.LessonService;
import music_center_backend.service.StudentMedalService;
import music_center_backend.service.StudentService;
import music_center_backend.service.TeacherService;

@ExtendWith(MockitoExtension.class)
class TeacherStudentControllerTest {

	@Mock
	private TeacherService teacherService;

    @Mock
    private StudentService studentService;

    @Mock
    private LessonService lessonService;

    @Mock
    private StudentMedalService studentMedalService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
                mockMvc = MockMvcBuilders.standaloneSetup(new TeacherController(teacherService, studentService, lessonService, studentMedalService))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
        @DisplayName("GET /teachers/me/students should return students")
        void getStudentsShouldReturnStudents() throws Exception {
        StudentResponse response = new StudentResponse("student-1", "Mia", LocalDate.of(2024, 1, 10), "Piano", "Ms. Lee", true);
                when(teacherService.getMyStudents()).thenReturn(List.of(response));

                mockMvc.perform(get("/api/v1/teachers/me/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pubilcId").value("student-1"))
                .andExpect(jsonPath("$[0].teacherName").value("Ms. Lee"));

                verify(teacherService).getMyStudents();
    }

    @Test
        @DisplayName("GET /teachers/me/students/{studentPublicId}/lessons should pass filters to the lesson service")
    void getStudentLessonsShouldDelegateFilters() throws Exception {
        LocalDate date = LocalDate.of(2025, 3, 5);
        LocalDate startDate = LocalDate.of(2025, 3, 1);
        LocalDate endDate = LocalDate.of(2025, 3, 31);
        LessonResponse response = new LessonResponse("lesson-1", date, State.PRESENT.name(), (short) 9, "Great lesson");
        when(lessonService.getLessons("student-1", date, startDate, endDate)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/teachers/me/students/{studentPublicId}/lessons", "student-1")
                        .param("date", date.toString())
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].publicId").value("lesson-1"))
                .andExpect(jsonPath("$[0].state").value("PRESENT"));

        verify(lessonService).getLessons("student-1", date, startDate, endDate);
    }

    @Test
        @DisplayName("GET /teachers/me/students/{studentPublicId}/medals should pass filters to the medal service")
    void getStudentMedalsShouldDelegateFilters() throws Exception {
        LocalDate startDate = LocalDate.of(2025, 4, 1);
        LocalDate endDate = LocalDate.of(2025, 4, 30);
        StudentMedalResponse response = new StudentMedalResponse("student-1", "Star", LocalDate.of(2025, 4, 12));
        when(studentMedalService.searchMedals("student-1", startDate, endDate)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/teachers/me/students/{studentPublicId}/medals", "student-1")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentPublicId").value("student-1"))
                .andExpect(jsonPath("$[0].medalName").value("Star"));

        verify(studentMedalService).searchMedals("student-1", startDate, endDate);
    }

    @Test
        @DisplayName("POST /teachers/me should create the student")
        void createStudentShouldDelegate() throws Exception {
        StudentResponse response = new StudentResponse("student-1", "Mia", LocalDate.of(2024, 1, 10), "Piano", "Ms. Lee", true);
        when(studentService.createStudent(any(CreateStudentRequest.class))).thenReturn(response);

        String body = """
                {
                  "name": "Mia",
                  "dateOfStart": "2024-01-10",
                  "instrument": "Piano",
                  "takingSolfeige": true,
                  "accessCode": "1234"
                }
                """;

                mockMvc.perform(post("/api/v1/teachers/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pubilcId").value("student-1"))
                .andExpect(jsonPath("$.teacherName").value("Ms. Lee"));

                verify(studentService).createStudent(any(CreateStudentRequest.class));
    }

    @Test
        @DisplayName("POST /teachers/me/students/{studentPublicId}/lessons should create a lesson")
    void createLessonShouldDelegateToLessonService() throws Exception {
        LessonResponse response = new LessonResponse("lesson-1", LocalDate.of(2025, 3, 5), State.EXCUSED.name(), (short) 7, "Needs work");
                when(lessonService.createLesson(eq("student-1"), any(CreateLessonRequest.class))).thenReturn(response);

        String body = """
                {
                  "date": "2025-03-05",
                  "state": "EXCUSED",
                  "mark": 7,
                  "note": "Needs work"
                }
                """;

        mockMvc.perform(post("/api/v1/teachers/me/students/{studentPublicId}/lessons", "student-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.publicId").value("lesson-1"))
                .andExpect(jsonPath("$.mark").value(7));

                verify(lessonService).createLesson(eq("student-1"), any(CreateLessonRequest.class));
    }

    @Test
        @DisplayName("POST /teachers/me/students/{studentPublicId}/medals should assign a medal")
    void assignMedalShouldDelegateToStudentMedalService() throws Exception {
        StudentMedalResponse response = new StudentMedalResponse("student-1", "Star", LocalDate.of(2025, 4, 12));
                when(studentMedalService.assign(eq("student-1"), any(AssignStudentMedalRequest.class))).thenReturn(response);

        String body = """
                {
                  "medalName": "Star",
                  "medalDate": "2025-04-12"
                }
                """;

        mockMvc.perform(post("/api/v1/teachers/me/students/{studentPublicId}/medals", "student-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentPublicId").value("student-1"))
                .andExpect(jsonPath("$.medalName").value("Star"));

                verify(studentMedalService).assign(eq("student-1"), any(AssignStudentMedalRequest.class));
    }

    @Test
        @DisplayName("PATCH /teachers/me/students/{studentPublicId} should update the student")
    void updateStudentShouldDelegateToStudentService() throws Exception {
        StudentResponse response = new StudentResponse("student-1", "Mia Updated", LocalDate.of(2024, 1, 10), "Violin", "Ms. Lee", false);
                when(studentService.updateStudent(eq("student-1"), eq(null), any(UpdateStudentRequest.class))).thenReturn(response);

        String body = """
                {
                  "name": "Mia Updated",
                  "instrument": "Violin",
                  "takingSolfeige": false
                }
                """;

        mockMvc.perform(patch("/api/v1/teachers/me/students/{studentPublicId}", "student-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mia Updated"));

                verify(studentService).updateStudent(eq("student-1"), eq(null), any(UpdateStudentRequest.class));
    }

    @Test
        @DisplayName("PATCH /teachers/me/students/{studentPublicId}/lessons/{lessonPublicId} should update the lesson")
    void updateLessonShouldDelegateToLessonService() throws Exception {
        LessonResponse response = new LessonResponse("lesson-1", LocalDate.of(2025, 3, 5), State.PRESENT.name(), (short) 10, "Improved");
                when(lessonService.updateLesson(eq("lesson-1"), eq("student-1"), any(UpdateLessonRequest.class))).thenReturn(response);

        String body = """
                {
                  "state": "PRESENT",
                  "mark": 10,
                  "note": "Improved"
                }
                """;

        mockMvc.perform(patch("/api/v1/teachers/me/students/{studentPublicId}/lessons/{lessonPublicId}", "student-1", "lesson-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicId").value("lesson-1"))
                .andExpect(jsonPath("$.mark").value(10));

                verify(lessonService).updateLesson(eq("lesson-1"), eq("student-1"), any(UpdateLessonRequest.class));
    }

    @Test
        @DisplayName("DELETE /teachers/me/students/{studentPublicId} should delete the student")
    void deleteStudentShouldDelegateToStudentService() throws Exception {
                mockMvc.perform(delete("/api/v1/teachers/me/students/{studentPublicId}", "student-1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(studentService).deleteStudent("student-1");
    }

    @Test
        @DisplayName("DELETE /teachers/me/students/{studentPublicId}/lessons/{lessonPublicId} should delete the lesson")
    void deleteLessonShouldDelegateToLessonService() throws Exception {
                mockMvc.perform(delete("/api/v1/teachers/me/students/{studentPublicId}/lessons/{lessonPublicId}", "student-1", "lesson-1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

                verify(lessonService).deleteLesson("lesson-1", "student-1");
    }

    @Test
        @DisplayName("DELETE /teachers/me/students/{studentPublicId}/medals/{medalName} should remove the medal")
    void removeMedalShouldDelegateToStudentMedalService() throws Exception {
        LocalDate medalDate = LocalDate.of(2025, 4, 12);

                mockMvc.perform(delete("/api/v1/teachers/me/students/{studentPublicId}/medals/{medalName}", "student-1", "Star")
                        .param("medalDate", medalDate.toString()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(studentMedalService).remove("student-1", "Star", medalDate);
    }
}