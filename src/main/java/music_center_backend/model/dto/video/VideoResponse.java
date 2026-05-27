package music_center_backend.model.dto.video;

import java.time.LocalDate;

public class VideoResponse {
    private String publicId;
    private String title;
    private String description;
    private String link;
    private LocalDate date;

    public VideoResponse(String publicId, String title, String description, String link, LocalDate date) {
        this.publicId = publicId;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
    }

    public String getPublicId() { return this.publicId; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getLink() { return this.link; }
    public LocalDate getDate() { return this.date; }
}
