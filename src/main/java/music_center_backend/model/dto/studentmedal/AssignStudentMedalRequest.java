package music_center_backend.model.dto.studentmedal;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class AssignStudentMedalRequest {
    @NotBlank
    private String studentPublicId;

    @NotBlank
    private String medalName;

    private LocalDate medalDate;

    public AssignStudentMedalRequest() {}

    public String getStudentPublicId() { return this.studentPublicId; }
    public String getMedalName() { return this.medalName; }
    public LocalDate getMedalDate() { return this.medalDate; }

    public void setStudentPublicId(String studentPublicId) { this.studentPublicId = studentPublicId; }
    public void setMedalName(String medalName) { this.medalName = medalName; }
    public void setMedalDate(LocalDate medalDate) { this.medalDate = medalDate; }
}
