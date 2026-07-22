package music_center_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import music_center_backend.model.entity.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("""
            SELECT l FROM Lesson l
            WHERE l.student.publicId = :studentPublicId
            AND (:date IS NULL OR l.date = :date)
            AND (:startDate IS NULL OR l.date >= :startDate)
            AND (:endDate IS NULL OR l.date <= :endDate)
    """)
    List<Lesson> getLessons(String studentPublicId, LocalDate date, LocalDate startDate, LocalDate endDate);

    List<Lesson> findByTeacher_PublicId(String teacherPublicId);
    Optional<Lesson> findByPublicId(String publicId);
    boolean existsByPublicIdAndTeacher_PublicIdAndStudent_PublicId(String lessonPublicId, String teacherPublicId, String studentPublicId);
}