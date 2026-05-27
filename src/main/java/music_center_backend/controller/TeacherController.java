package music_center_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import music_center_backend.model.dto.teacher.CreateTeacherRequest;
import music_center_backend.model.dto.teacher.TeacherResponse;
import music_center_backend.model.dto.teacher.UpdateTeacherRequest;
import music_center_backend.service.TeacherService;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<TeacherResponse> getAllTeachers() {
        return teacherService.getAll();
    }

    @GetMapping("/{publicId}")
    public TeacherResponse getTeacher(@PathVariable String publicId) {
        return teacherService.getByPublicId(publicId);
    }

    @PostMapping
    public ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody CreateTeacherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(request));
    }

    @PatchMapping("/{publicId}")
    public TeacherResponse updateTeacher(@PathVariable String publicId, @Valid @RequestBody UpdateTeacherRequest request) {
        return teacherService.updateTeacher(publicId, request);
    }
}
