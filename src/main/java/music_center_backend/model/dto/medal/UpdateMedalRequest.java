package music_center_backend.model.dto.medal;

import jakarta.validation.constraints.Size;

public class UpdateMedalRequest {
    @Size(min = 1, max = 255)
    private String medalName;

    @Size(min = 1)
    private String medalDescription;

    public UpdateMedalRequest() {}

    public String getMedalName() { return this.medalName; }
    public String getMedalDescription() { return this.medalDescription; }

    public void setMedalName(String medalName) { this.medalName = medalName; }
    public void setMedalDescription(String medalDescription) { this.medalDescription = medalDescription; }
}
