package music_center_backend.model.dto.profile;

import java.util.List;

import music_center_backend.model.dto.student.StudentResponse;
import music_center_backend.model.dto.studentmedal.StudentMedalResponse;

public class StudentProfileResponse {
    private StudentResponse student;
    private List<StudentMedalResponse> medals;

    public StudentProfileResponse(StudentResponse student, List<StudentMedalResponse> medals) {
        this.student = student;
        this.medals = medals;
    }

    public StudentResponse getStudent() { return student; }
    public List<StudentMedalResponse> getMedals() { return medals; }
}
