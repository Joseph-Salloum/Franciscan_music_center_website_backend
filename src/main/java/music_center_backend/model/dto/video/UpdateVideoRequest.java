package music_center_backend.model.dto.video;

import jakarta.validation.constraints.Size;

public class UpdateVideoRequest {
    @Size(min = 1, max = 255)
    private String title;

    @Size(min = 1)
    private String description;

    @Size(min = 1)
    private String link;

    public UpdateVideoRequest() {}

    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getLink() { return this.link; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLink(String link) { this.link = link; }
}
