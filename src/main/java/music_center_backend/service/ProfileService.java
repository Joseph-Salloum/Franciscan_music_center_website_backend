package music_center_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import music_center_backend.model.dto.profile.StudentProfileResponse;
import music_center_backend.model.dto.student.StudentResponse;
import music_center_backend.model.dto.studentmedal.StudentMedalResponse;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final StudentService studentService;
    private final StudentMedalService studentMedalService;

    public StudentProfileResponse getStudentProfile(String studentPublicId) {
        StudentResponse student = studentService.getByPublicId(studentPublicId);

        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);
        List<StudentMedalResponse> medals = studentMedalService.searchMedals(studentPublicId, startDate, endDate);

        return new StudentProfileResponse(student, medals);
    }
}
