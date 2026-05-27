package music_center_backend.model.dto.mark;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;

public class CreateMarkRequest {    
    @PositiveOrZero
    @Max(10)
    private short mark;

    private LocalDate date;

    private boolean instrument;

    public CreateMarkRequest() {}

    public short getMark() { return this.mark; }
    public LocalDate getDate() { return this.date; }
    public boolean isInstrument() { return this.instrument; }

    public void setMark(short mark) { this.mark = mark; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setInstrument(boolean instrument) { this.instrument = instrument; }
}
