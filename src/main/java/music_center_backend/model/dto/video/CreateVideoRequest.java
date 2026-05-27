package music_center_backend.model.dto.video;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateVideoRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String link;
    
    private LocalDate date;

    public CreateVideoRequest() {}

    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getLink() { return this.link; }
    public LocalDate getDate() { return this.date; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLink(String link) { this.link = link; }
    public void setDate(LocalDate date) { this.date = date; }
}
