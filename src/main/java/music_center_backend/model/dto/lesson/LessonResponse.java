package music_center_backend.model.dto.lesson;

import java.time.LocalDate;

public class LessonResponse {
    private String publicId;
    private LocalDate date;
    private String state;
    private short mark;
    private String note;

    public LessonResponse(String publicId, LocalDate date, String state, short mark, String note) {
        this.publicId = publicId;
        this.date = date;
        this.state = state;
        this.mark = mark;
        this.note = note;
    }

    public String getPublicId() { return this.publicId; }
    public LocalDate getDate() { return this.date; }
    public String getState() { return this.state; }
    public short getMark() { return this.mark; }
    public String getNote() { return this.note; }
}
