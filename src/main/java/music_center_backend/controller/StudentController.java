package music_center_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import music_center_backend.model.dto.lesson.LessonResponse;
import music_center_backend.model.dto.profile.StudentProfileResponse;
import music_center_backend.service.LessonService;
import music_center_backend.service.ProfileService;

@RestController
@RequestMapping("/api/v1/students/me")
@RequiredArgsConstructor
public class StudentController {
    private final ProfileService profileService;
    private final LessonService lessonService;

    @GetMapping("/profile")
    public StudentProfileResponse getProfile() {
        return profileService.getMyStudentProfile();
    }
    @GetMapping("/lessons")
    public List<LessonResponse> getLessons(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return lessonService.getMyLessons(date, startDate, endDate);
    }
}
