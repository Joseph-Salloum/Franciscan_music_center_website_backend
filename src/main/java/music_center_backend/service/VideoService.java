package music_center_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import music_center_backend.exception.exceptions.VideoNotFoundException;
import music_center_backend.model.dto.video.CreateVideoRequest;
import music_center_backend.model.dto.video.UpdateVideoRequest;
import music_center_backend.model.dto.video.VideoResponse;
import music_center_backend.model.entity.Video;
import music_center_backend.repository.VideoRepository;
import music_center_backend.util.HashGenerator;

@Service
@Transactional(readOnly = true)
public class VideoService {
    private final VideoRepository videoRepository; 

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<VideoResponse> getAll() {
        return toResponse(videoRepository.findAll());
    }
    public List<VideoResponse> searchVideos(String title, LocalDate after, LocalDate startDate, LocalDate endDate) {
        return toResponse(videoRepository.searchVideos(title, after, startDate, endDate));
    }
    public VideoResponse getByPublicId(String publicId) {
        Video video = videoRepository.findByPublicId(publicId)
                            .orElseThrow(() -> new VideoNotFoundException("No video with publicId " + publicId));
        return toResponse(video);
    }

    @Transactional
    public VideoResponse createVideo(CreateVideoRequest request) {
        String publicId = createPublicId();

        if (!isValidLink(request.getLink())) {
            throw new IllegalArgumentException("The link must start with http:// or https://");
        }

        Video newVideo = mapFromCreateRequest(publicId, request);
        return toResponse(videoRepository.save(newVideo));
    }

    @Transactional
    public VideoResponse updateVideo(String publicId, UpdateVideoRequest request) {
        Video video = videoRepository.findByPublicId(publicId)
                            .orElseThrow(() -> new VideoNotFoundException("No video with publicId " + publicId));

        if (request.getTitle() != null && !request.getTitle().equals(video.getTitle())) {
            video.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            video.setDescription(request.getDescription());
        }
        if (request.getLink() != null) {
            if (!isValidLink(request.getLink())) {
                throw new IllegalArgumentException("The link must start with http:// or https://");
            }
            video.setLink(request.getLink());
        }

        return toResponse(video);
    }

    @Transactional
    public void deleteVideo(String publicId) {
        Video video = videoRepository.findByPublicId(publicId)
                            .orElseThrow(() -> new VideoNotFoundException("No video with title " + publicId));
        videoRepository.deleteById(video.getId());
    }
    
    private Video mapFromCreateRequest(String publicId, CreateVideoRequest request) {
        Video video = new Video(publicId, request.getTitle(), request.getDescription(), request.getLink(), request.getDate());
        return video;
    }
    private VideoResponse toResponse(Video video) {
        return new VideoResponse(
                        video.getPublicId(),
                        video.getTitle(),
                        video.getDescription(),
                        video.getLink(),
                        video.getDate()
                    );
    }
    private List<VideoResponse> toResponse(List<Video> videos) {
        return videos.stream().map(this::toResponse).toList();
    }
    private boolean isValidLink(String link) {
        return link.startsWith("https://") || link.startsWith("http://");
    }
    private String createPublicId() {
        return HashGenerator.generateRandomHash(12);
    }
}
