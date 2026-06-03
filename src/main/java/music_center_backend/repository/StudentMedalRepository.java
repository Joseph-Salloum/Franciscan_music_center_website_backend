package music_center_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import music_center_backend.model.entity.StudentMedal;

@Repository
public interface StudentMedalRepository extends JpaRepository<StudentMedal, Long> {
    @Query("""
        SELECT sm FROM StudentMedal sm
        WHERE (:studentPublicId IS NULL OR sm.student.publicId = :studentPublicId)
        AND (:startDate IS NULL OR sm.medalDate >= :startDate)
        AND (:endDate IS NULL OR sm.medalDate <= :endDate)
    """)
    List<StudentMedal> searchStudentMedals(String studentPublicId, LocalDate startDate, LocalDate endDate);
    
    List<StudentMedal> findByMedalDate(LocalDate medalDate);
    List<StudentMedal> findByMedal_MedalName(String medalName);
    List<StudentMedal> findByStudent_PublicId(String studentPublicId);

    Optional<StudentMedal> findByStudent_PublicIdAndMedal_MedalNameAndMedalDate(String studentPublicId, String medalName, LocalDate medalDate);
}
