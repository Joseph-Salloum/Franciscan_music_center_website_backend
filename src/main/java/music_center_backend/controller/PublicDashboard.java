package music_center_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import music_center_backend.model.dto.medal.MedalResponse;
import music_center_backend.model.dto.profile.TeacherPublicProfileResponse;
import music_center_backend.model.dto.video.VideoResponse;
import music_center_backend.service.MedalService;
import music_center_backend.service.TeacherService;
import music_center_backend.service.VideoService;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class PublicDashboard {
    private final MedalService medalService;
    private final VideoService videoService;
    private final TeacherService teacherService;

    @GetMapping("/medals")
    public List<MedalResponse> getAllMedals() {
        return medalService.getAllMedals();
    }
    @GetMapping("/medals/{medalName}")
    public MedalResponse getByMedalName(@PathVariable String medalName) {
        return medalService.getByMedalName(medalName);
    }

    @GetMapping("/videos")
    public List<VideoResponse> getAllVideos() {
        return videoService.getAllVideos();
    }
    @GetMapping("/videos/search")
    public List<VideoResponse> searchVideos(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate after,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return videoService.searchVideos(title, after, startDate, endDate);
    }
    @GetMapping("/videos/{videoPublicId}")
    public VideoResponse getByPublicId(@PathVariable String videoPublicId) {
        return videoService.getByPublicId(videoPublicId);
    }

    @GetMapping("/teachers")
    public List<TeacherPublicProfileResponse> getTeachersProfiles() {
        return teacherService.getPublicProfiles();
    }
}
