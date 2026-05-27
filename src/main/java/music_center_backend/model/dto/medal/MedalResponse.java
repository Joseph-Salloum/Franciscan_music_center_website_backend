package music_center_backend.model.dto.medal;

public class MedalResponse {
    private String medalName;
    private String medalDescription;

    public MedalResponse(String medalName, String medalDescription) {
        this.medalName = medalName;
        this.medalDescription = medalDescription;
    }

    public String getMedalName() { return this.medalName; }
    public String getMedalDescription() { return this.medalDescription; }
}
