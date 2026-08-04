package music_center_backend.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private String publicId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "link", nullable = false, unique = true)
    private String link;

    @Column(name = "date", nullable = false, updatable = false)
    private LocalDate date;

    protected Video() {}

    public Video(String publicId, String title, String description, String link, LocalDate date) {
        this.publicId = publicId;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = (date == null) ? LocalDate.now() : date;
    }

    public Long getId() { return this.id; }
    public String getPublicId() { return this.publicId; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getLink() { return this.link; }
    public LocalDate getDate() { return this.date; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLink(String link) { this.link = link; }
}
