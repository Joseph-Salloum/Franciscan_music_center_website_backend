package music_center_backend.model.dto.medal;

import jakarta.validation.constraints.NotBlank;

public class CreateMedalRequest {
    @NotBlank(message = "Medal name is required")
    private String medalName;
    
    @NotBlank(message = "Medal description is required")
    private String medalDescription;

    public CreateMedalRequest() {}

    public String getMedalName() { return this.medalName; }
    public String getMedalDescription() { return this.medalDescription; }

    public void setMedalName(String medalName) { this.medalName = medalName; }
    public void setMedalDescription(String medalDescription) { this.medalDescription = medalDescription; }
}
