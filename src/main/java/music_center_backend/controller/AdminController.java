package music_center_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import lombok.RequiredArgsConstructor;
import music_center_backend.model.dto.lesson.LessonResponse;
import music_center_backend.model.dto.medal.CreateMedalRequest;
import music_center_backend.model.dto.medal.MedalResponse;
import music_center_backend.model.dto.medal.UpdateMedalRequest;
import music_center_backend.model.dto.student.CreateStudentRequest;
import music_center_backend.model.dto.student.StudentResponse;
import music_center_backend.model.dto.student.UpdateStudentRequest;
import music_center_backend.model.dto.studentmedal.AssignStudentMedalRequest;
import music_center_backend.model.dto.studentmedal.StudentMedalResponse;
import music_center_backend.model.dto.teacher.CreateTeacherRequest;
import music_center_backend.model.dto.teacher.TeacherResponse;
import music_center_backend.model.dto.teacher.UpdateTeacherRequest;
import music_center_backend.model.dto.video.CreateVideoRequest;
import music_center_backend.model.dto.video.UpdateVideoRequest;
import music_center_backend.model.dto.video.VideoResponse;
import music_center_backend.service.LessonService;
import music_center_backend.service.MedalService;
import music_center_backend.service.StudentMedalService;
import music_center_backend.service.StudentService;
import music_center_backend.service.TeacherService;
import music_center_backend.service.VideoService;
import music_center_backend.util.ValidationGroups;

@RestController
@RequestMapping("/api/v1/admin/me")
@RequiredArgsConstructor
public class AdminController {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final MedalService medalService;
    private final StudentMedalService studentMedalService;
    private final VideoService videoService;
    private final LessonService lessonService;

    @GetMapping("/profile")
    public TeacherResponse getMyProfile() {
        return teacherService.getMyProfile();
    }
    @GetMapping("/teachers")
    public List<TeacherResponse> getAllTeachers() {
        //  make sure to exclude self account from this list
        return teacherService.getAll();
    }
    @GetMapping("/teachers/{teacherPublicId}/students")
    public List<StudentResponse> getTeacherStudents(@PathVariable String teacherPublicId) {
        return studentService.getByTeacherPublicId(teacherPublicId);
    }
    @GetMapping("/teachers/{teacherPublicId}/students/{studentPublicId}/lessons")
    public List<LessonResponse> getStudentLessons(
            @PathVariable String studentPublicId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return lessonService.getLessons(studentPublicId, date, startDate, endDate);
    }
    @GetMapping("/medals")
    public List<MedalResponse> getAllMedals() {
        return medalService.getAllMedals();
    }
    @GetMapping("/medals-history")
    public List<StudentMedalResponse> getAllStudentMedals() {
        //  this endpoint will work as a getter for assigning and removing history
        return studentMedalService.getAll();
    }
    @GetMapping("/teachers/{teacherPublicId}/students/{studentPublicId}/medals")
    public List<StudentMedalResponse> getStudentMedals(
            @PathVariable String studentPublicId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return studentMedalService.searchMedals(studentPublicId, startDate, endDate);
    }

    @PostMapping("/teachers")
    public ResponseEntity<TeacherResponse> addTeacher(@Valid @RequestBody CreateTeacherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(request));
    }
    @PostMapping("/teacher/{teacherPublicId}/students")
    public ResponseEntity<StudentResponse> addStudent(@Validated({ValidationGroups.AdminCreateStudent.class, Default.class}) @RequestBody CreateStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(request));
    }
    @PostMapping("/medals")
    public ResponseEntity<MedalResponse> addMedal(@Valid @RequestBody CreateMedalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medalService.createMedal(request));
    }
    @PostMapping("/videos")
    public ResponseEntity<VideoResponse> addVideo(@Valid @RequestBody CreateVideoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(videoService.createVideo(request));
    }
    @PostMapping("/teachers/{teacherPublicId}/students/{studentPublicId}/medals")
    public StudentMedalResponse assignMedal(
            @PathVariable String studentPublicId,
            @Valid @RequestBody AssignStudentMedalRequest request) {
        
        return studentMedalService.assign(studentPublicId, request);
    }

    @PatchMapping("/teachers/{teacherPublicId}")
    public TeacherResponse updateTeacher(
            @PathVariable String teacherPublicId,
            @Valid @RequestBody UpdateTeacherRequest request) {

        return teacherService.updateTeacher(teacherPublicId, request);
    }
    @PatchMapping("/teachers/{teacherPublicId}/students/{studentPublicId}")
    public StudentResponse updateStudent(
            @PathVariable String teacherPublicId,
            @PathVariable String studentPublicId,
            @Valid @RequestBody UpdateStudentRequest request) {

        return studentService.updateStudent(studentPublicId, teacherPublicId, request);
    }
    @PatchMapping("/medals/{medalName}")
    public MedalResponse updateMedal(
            @PathVariable String medalName,
            @Valid @RequestBody UpdateMedalRequest request) {

        return medalService.updateMedal(medalName, request);
    }
    @PatchMapping("/videos/{videoPublicId}")
    public VideoResponse updateVideo(
            @PathVariable String videoPublicId,
            @Valid @RequestBody UpdateVideoRequest request) {

        return videoService.updateVideo(videoPublicId, request);
    }

    @DeleteMapping("/teachers/{teacherPublicId}")
    public ResponseEntity<Void> deleteTeacher(
            @PathVariable String teacherPublicId) {

        teacherService.deleteTeacher(teacherPublicId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/teachers/{teacherPublicId}/students/{studentPublicId}")
    public ResponseEntity<Void> delteStudent(
            @PathVariable String studentPublicId) {
        
        studentService.deleteStudent(studentPublicId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/medals/{medalName}")
    public ResponseEntity<Void> delteMedal(
            @PathVariable String medalName) {

        medalService.deleteMedal(medalName);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/videos/{videoPublicId}")
    public ResponseEntity<Void> deleteVideo(
            @PathVariable String videoPublicId) {

        videoService.deleteVideo(videoPublicId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/teachers/{teacherPublicId}/students/{studentPublicId}/medals/{medalName}")
    public ResponseEntity<Void> removeMedal(
            @PathVariable String studentPublicId,
            @PathVariable String medalName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate medalDate) {

        studentMedalService.remove(studentPublicId, medalName, medalDate);
        return ResponseEntity.noContent().build();
    }
}
