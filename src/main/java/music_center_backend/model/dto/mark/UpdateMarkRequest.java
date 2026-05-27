package music_center_backend.model.dto.mark;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;

public class UpdateMarkRequest {
    @PositiveOrZero
    @Max(10)
    private short mark;

    public UpdateMarkRequest() {}

    public short getMark() { return this.mark; }

    public void setMark(short mark) { this.mark = mark; }
}
