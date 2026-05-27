package music_center_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import music_center_backend.model.entity.Mark;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    @Query("""
            SELECT m FROM Mark m
            WHERE m.student.publicId = :studentPublicId
            AND (:date IS NULL OR m.date = :date)
            AND (:startDate IS NULL OR m.date >= :startDate)
            AND (:endDate IS NULL OR m.date <= :endDate)
    """)
    List<Mark> getMarks(String studentPublicId, LocalDate date, LocalDate startDate, LocalDate endDate);
    
    List<Mark> findByTeacherPublicId(String teacherPublicId);
    Optional<Mark> findByPublicId(String publicId);
}
