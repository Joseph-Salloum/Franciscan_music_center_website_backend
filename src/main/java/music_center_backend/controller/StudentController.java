package music_center_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.groups.Default;
import music_center_backend.model.dto.student.CreateStudentRequest;
import music_center_backend.model.dto.student.StudentResponse;
import music_center_backend.service.StudentService;
import music_center_backend.util.ValidationGroups;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentResponse> getAllStudents() {
        return studentService.getAll();
    }

    @GetMapping("/{publicId}")
    public StudentResponse getStudent(@PathVariable String publicId) {
        return studentService.getByPublicId(publicId);
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Validated({ValidationGroups.NormalStudentCreation.class, Default.class}) @RequestBody CreateStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(request));
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String publicId) {
        studentService.deleteStudent(publicId);
        return ResponseEntity.noContent().build();
    }
}
