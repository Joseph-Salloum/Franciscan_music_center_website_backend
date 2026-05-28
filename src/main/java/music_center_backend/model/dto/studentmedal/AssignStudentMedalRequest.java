package music_center_backend.model.dto.studentmedal;

import jakarta.validation.constraints.NotBlank;

public class AssignStudentMedalRequest {
    @NotBlank
    private String studentPublicId;

    @NotBlank
    private String medalName;

    public AssignStudentMedalRequest() {}

    public String getStudentPublicId() { return this.studentPublicId; }
    public String getMedalName() { return this.medalName; }

    public void setStudentPublicId(String studentPublicId) { this.studentPublicId = studentPublicId; }
    public void setMedalName(String medalName) { this.medalName = medalName; }
}
