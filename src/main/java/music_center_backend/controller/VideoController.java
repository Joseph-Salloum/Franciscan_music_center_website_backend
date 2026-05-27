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
import music_center_backend.model.dto.video.CreateVideoRequest;
import music_center_backend.model.dto.video.UpdateVideoRequest;
import music_center_backend.model.dto.video.VideoResponse;
import music_center_backend.service.VideoService;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {
    private final VideoService videoService;
    
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public List<VideoResponse> getAll() {
        return videoService.getAll();
    }

    @GetMapping("/search")
    public List<VideoResponse> searchVideos(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate after,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return videoService.searchVideos(title, after, startDate, endDate);
    }

    @GetMapping("/{publicId}")
    public VideoResponse getByPublicId(@PathVariable String publicId) {
        return videoService.getByPublicId(publicId);
    }

    @PostMapping
    public ResponseEntity<VideoResponse> createVideo(@Valid @RequestBody CreateVideoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(videoService.createVideo(request));
    }

    @PatchMapping("/{publicId}")
    public VideoResponse updateVideo(@PathVariable String publicId, @Valid @RequestBody UpdateVideoRequest request) {
        return videoService.updateVideo(publicId, request);
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable String publicId) {
        videoService.deleteVideo(publicId);
        return ResponseEntity.noContent().build();
    }
}
