package music_center_backend.model.dto.lesson;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import music_center_backend.model.constants.State;

public class CreateLessonRequest {
    private LocalDate date;

    @NotNull
    private State state;

    @PositiveOrZero
    @Max(10)
    @NotNull
    private short mark;

    private String note;

    public CreateLessonRequest() {}

    public LocalDate getDate() { return this.date; }
    public State getState() { return this.state; }
    public short getMark() { return this.mark; }
    public String getNote() { return this.note; }

    public void setDate(LocalDate date) { this.date = date; }
    public void setState(State state) { this.state = state; }
    public void setMark(short mark) { this.mark = mark; }
    public void setNote(String note) { this.note = note; }
}
