package music_center_backend.model.dto.student;

import java.time.LocalDate;

public class StudentResponse {
    private String pubilcId;
    private String name;
    private LocalDate dateOfStart;
    private String instrument;
    private String teacherName;
    private boolean takingSolfeige;

    public StudentResponse(String pubilcId, String name, LocalDate dateOfStart, String instrument, String teacherName,
            boolean takingSolfeige) {

        this.pubilcId = pubilcId;
        this.name = name;
        this.dateOfStart = dateOfStart;
        this.instrument = instrument;
        this.teacherName = teacherName;
        this.takingSolfeige = takingSolfeige;
    }

    public String getPubilcId() { return this.pubilcId; }
    public String getName() { return this.name; }
    public LocalDate getDateOfStart() { return this.dateOfStart; }
    public String getInstrument() { return this.instrument; }
    public String getTeacherName() { return this.teacherName; }
    public boolean isTakingSolfeige() { return this.takingSolfeige; }
}
