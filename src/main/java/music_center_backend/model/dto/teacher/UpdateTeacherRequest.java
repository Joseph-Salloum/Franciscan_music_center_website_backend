package music_center_backend.model.dto.teacher;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import music_center_backend.model.constant.Specialization;

public class UpdateTeacherRequest {
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    private Specialization specialization;

    private boolean admin;

    public UpdateTeacherRequest() {}

    public String getName() { return this.name; }
    public Specialization getSpecialization() { return this.specialization; }
    public Boolean getAdmin() { return this.admin; }

    public void setName(String name) { this.name = name; }
    public void setSpecialization(Specialization specialization) { this.specialization = specialization; }
    public void setAdmin(boolean admin) { this.admin = admin; }
}
