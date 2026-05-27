package music_center_backend.model.dto.lesson;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import music_center_backend.model.constants.State;

public class UpdateLessonRequest {
    private State state;

    @PositiveOrZero
    @Max(10)
    private short mark;

    private String note;

    public UpdateLessonRequest() {}

    public State getState() { return this.state; }
    public short getMark() { return this.mark; }
    public String getNote() { return this.note; }

    public void setState(State state) { this.state = state; }
    public void setMark(short mark) { this.mark = mark; }
    public void setNote(String note) { this.note = note; }
}
