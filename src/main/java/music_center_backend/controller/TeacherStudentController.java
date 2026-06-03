package music_center_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;

import org.springframework.validation.annotation.Validated;

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
import music_center_backend.util.ValidationGroups;

@RestController
@RequestMapping("/api/v1/teachers/{teacherPublicId}/students")
public class TeacherStudentController {
    private final StudentService studentService;
    private final LessonService lessonService;
    private final StudentMedalService studentMedalService;

    public TeacherStudentController(StudentService studentService, LessonService lessonService, StudentMedalService studentMedalService) {
        this.studentService = studentService;
        this.lessonService = lessonService;
        this.studentMedalService = studentMedalService;
    }

    @GetMapping
    public List<StudentResponse> getStudents(@PathVariable String teacherPublicId) {
        return studentService.getByTeacherPublicId(teacherPublicId);
    }
    @GetMapping("/{studentPublicId}/lessons")
    public List<LessonResponse> getStudentLessons(
            @PathVariable String studentPublicId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return lessonService.getLessons(studentPublicId, date, startDate, endDate);
    }
    @GetMapping("/{studentPublicId}/medals")
    public List<StudentMedalResponse> getStudentMedals(
            @PathVariable String studentPublicId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return studentMedalService.searchMedals(studentPublicId, startDate, endDate);
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @PathVariable String teacherPublicId,
            @Validated({ValidationGroups.TeacherStudentCreation.class, Default.class}) @RequestBody CreateStudentRequest request) {
        
        request.setTeacherPublicId(teacherPublicId);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(request));
    }
    @PostMapping("/{studentPublicId}/lessons")
    public ResponseEntity<LessonResponse> createLesson(
            @PathVariable String teacherPublicId,
            @PathVariable String studentPublicId,
            @Valid @RequestBody CreateLessonRequest request) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.createLesson(teacherPublicId, studentPublicId, request));
    }
    @PostMapping("/{studentPublicId}/medals")
    public StudentMedalResponse assignMedal(@Valid @RequestBody AssignStudentMedalRequest request) {
        return studentMedalService.assign(request);
    }

    @PatchMapping("/{studentPublicId}")
    public StudentResponse updateStudent(
            @PathVariable String teacherPublicId,
            @PathVariable String studentPublicId,
            @RequestBody UpdateStudentRequest request) {

        return studentService.updateStudent(studentPublicId, teacherPublicId, request);
    }
    @PatchMapping("/{studentPublicId}/lessons/{lessonPublicId}")
    public LessonResponse updateLesson(
            @PathVariable String teacherPublicId,
            @PathVariable String studentPublicId,
            @PathVariable String lessonPublicId,
            @RequestBody UpdateLessonRequest request) {

        return lessonService.updateLesson(lessonPublicId, teacherPublicId, studentPublicId, request);
    }

    @DeleteMapping("/{studentPublicId}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable String studentPublicId) {

        studentService.deleteStudent(studentPublicId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{studentPublicId}/lessons/{lessonPublicId}")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable String teacherPublicId,
            @PathVariable String studentPublicId,
            @PathVariable String lessonPublicId) {

        lessonService.deleteLesson(lessonPublicId, teacherPublicId, studentPublicId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{studentPublicId}/medals/{medalName}")
    public ResponseEntity<Void> removeMedal(@PathVariable String studentPublicId, @PathVariable String medalName, @RequestParam LocalDate medalDate) {
        studentMedalService.remove(studentPublicId, medalName, medalDate);
        return ResponseEntity.noContent().build();
    }
}
