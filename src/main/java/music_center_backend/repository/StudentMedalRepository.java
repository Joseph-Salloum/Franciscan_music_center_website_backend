package music_center_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import music_center_backend.model.entity.StudentMedal;

@Repository
public interface StudentMedalRepository extends JpaRepository<StudentMedal, Long> {
    //  TODO: fix query problem
    // @Query("""
    //     SELECT sm FROM StudentMedal sm
    //     WHERE (:startDate IS NULL OR sm.medalDate >= :startDate)
    //     AND (:endDate IS NULL OR sm.medalDate <= endDate)
    // """)
    // List<StudentMedal> searchStudentMedals(LocalDate startDate, LocalDate endDate);
    
    List<StudentMedal> findByMedalDate(LocalDate medalDate);
    List<StudentMedal> findByMedalMedalName(String medalName);
    List<StudentMedal> findByStudentPublicId(String studentPublicId);
}
