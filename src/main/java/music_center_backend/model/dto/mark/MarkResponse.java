package music_center_backend.model.dto.mark;

import java.time.LocalDate;

public class MarkResponse {
    private String publicId;
    private short mark;
    private LocalDate date;
    private boolean instrument;

    public MarkResponse(String publicId, short mark, LocalDate date, boolean instrument) {
        this.publicId = publicId;
        this.mark = mark;
        this.date = date;
        this.instrument = instrument;
    }

    public String getPublicId() { return this.publicId; }
    public short getMark() { return this.mark; }
    public LocalDate getDate() { return this.date; }
    public boolean isInstrument() { return this.instrument; }
}
