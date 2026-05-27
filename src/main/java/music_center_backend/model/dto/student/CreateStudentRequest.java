package music_center_backend.model.dto.student;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import music_center_backend.util.ValidationGroups;

public class CreateStudentRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    private LocalDate dateOfStart;

    @NotBlank
    @Size(max = 255)
    private String instrument;

    @NotBlank(groups = ValidationGroups.NormalStudentCreation.class)
    @Null(groups = ValidationGroups.TeacherStudentCreation.class)
    private String teacherPublicId;

    private boolean takingSolfeige;

    @NotBlank
    @Size(min = 4, max = 8)
    private String accessCode;

    public CreateStudentRequest() {}

    public String getName() { return this.name; }
    public LocalDate getDateOfStart() { return this.dateOfStart; }
    public String getInstrument() { return this.instrument; }
    public String getTeacherPublicId() { return this.teacherPublicId; }
    public boolean isTakingSolfeige() { return this.takingSolfeige; }
    public String getAccessCode() { return this.accessCode; }

    public void setName(String name) { this.name = name; }
    public void setDateOfStart(LocalDate dateOfStart) { this.dateOfStart = dateOfStart; }
    public void setInstrument(String instrument) { this.instrument = instrument; }
    public void setTeacherPublicId(String teacherPublicId) { this.teacherPublicId = teacherPublicId; }
    public void setTakingSolfeige(boolean takingSolfeige) { this.takingSolfeige = takingSolfeige; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }
}
