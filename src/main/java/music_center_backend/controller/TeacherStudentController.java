package music_center_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import jakarta.validation.groups.Default;

import org.springframework.validation.annotation.Validated;

import music_center_backend.model.dto.lesson.CreateLessonRequest;
import music_center_backend.model.dto.lesson.LessonResponse;
import music_center_backend.model.dto.lesson.UpdateLessonRequest;
import music_center_backend.model.dto.student.CreateStudentRequest;
import music_center_backend.model.dto.student.StudentResponse;
import music_center_backend.model.dto.student.UpdateStudentRequest;
import music_center_backend.service.LessonService;
import music_center_backend.service.StudentService;
import music_center_backend.util.ValidationGroups;

@RestController
@RequestMapping("/api/v1/teachers/{teacherPublicId}/students")
public class TeacherStudentController {
    private final StudentService studentService;
    private final LessonService lessonService;

    public TeacherStudentController(StudentService studentService, LessonService lessonService) {
        this.studentService = studentService;
        this.lessonService = lessonService;
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

    @PostMapping
    public StudentResponse createStudent(
            @PathVariable String teacherPublicId,
            @Validated({ValidationGroups.TeacherStudentCreation.class, Default.class}) @RequestBody CreateStudentRequest request) {
        request.setTeacherPublicId(teacherPublicId);
        return studentService.createStudent(request);
    }
    @PostMapping("/{studentPublicId}/lessons")
    public LessonResponse createLesson(
            @PathVariable String teacherPublicId,
            @PathVariable String studentPublicId,
            @RequestBody CreateLessonRequest request) {
        return lessonService.createLesson(teacherPublicId, studentPublicId, request);
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
}
