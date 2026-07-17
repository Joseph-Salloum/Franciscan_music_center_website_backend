package music_center_backend.security;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import music_center_backend.repository.LessonRepository;
import music_center_backend.repository.StudentRepository;

@Component("authoritiesChecker")
@RequiredArgsConstructor
public class GlobalAuthoritiesChecker {
    private final CurrentUserService currentUserService;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    public boolean currentOwnStudent(String studentPublicId) {
        return studentRepository.existsByPublicIdAndTeacher_PublicId(
                studentPublicId, 
                currentUserService.getPublicId()
        );
    }

    public boolean canAccessStudentLesson(String lessonPublicId, String studentPublicId) {
        return lessonRepository.existsByPublicIdAndTeacher_PublicIdAndStudent_PublicId(
                lessonPublicId,
                currentUserService.getPublicId(),
                studentPublicId
        );
    }
}
