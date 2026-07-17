package music_center_backend.model.dto.student;

import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import music_center_backend.util.ValidationGroups;

public class UpdateStudentRequest {
    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String instrument;
    
    @Size(max = 255)
    @Null(groups = ValidationGroups.TeacherUpdateStudent.class)
    private String teacherPublicId;

    private Boolean takingSolfeige;

    public UpdateStudentRequest() {}

    public String getName() { return this.name; }
    public String getInstrument() { return this.instrument; }
    public String getTeacherPublicId() { return this.teacherPublicId; }
    public Boolean getTakingSolfeige() { return this.takingSolfeige; }

    public void setName(String name) { this.name = name; }
    public void setInstrument(String instrument) { this.instrument = instrument; }
    public void setTeacherPublicId(String teacherPublicId) { this.teacherPublicId = teacherPublicId; }
    public void setTakingSolfeige(Boolean takingSolfeige) { this.takingSolfeige = takingSolfeige; }
}
