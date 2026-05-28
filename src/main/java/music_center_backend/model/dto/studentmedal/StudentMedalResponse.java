package music_center_backend.model.dto.studentmedal;

import java.time.LocalDate;

public class StudentMedalResponse {
    private String studentPublicId;
    private String medalName;
    private LocalDate assignedAt;

    public StudentMedalResponse(String studentPublicId, String medalName, LocalDate assignedAt) {
        this.studentPublicId = studentPublicId;
        this.medalName = medalName;
        this.assignedAt = assignedAt;
    }

    public String getStudentPublicId() { return this.studentPublicId; }
    public String getMedalName() { return this.medalName; }
    public LocalDate getAssignedAt() { return this.assignedAt; }
}
