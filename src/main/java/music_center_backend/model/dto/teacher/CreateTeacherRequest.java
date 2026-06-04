package music_center_backend.model.dto.teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import music_center_backend.model.constant.Specialization;

public class CreateTeacherRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    private Specialization specialization;

    private boolean admin;

    @NotBlank
    @Size(min = 4, max = 8)
    private String accessCode;

    public CreateTeacherRequest() {}

    public String getName() { return this.name; }
    public Specialization getSpecialization() { return this.specialization; }
    public boolean isAdmin() { return this.admin; }
    public String getAccessCode() { return this.accessCode; }

    public void setName(String name) { this.name = name; }
    public void setSpecialization(Specialization specialization) { this.specialization = specialization; }
    public void setAdmin(boolean admin) { this.admin = admin; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }
}
