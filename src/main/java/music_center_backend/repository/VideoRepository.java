package music_center_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import music_center_backend.model.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("""
        SELECT v FROM Video v
        WHERE (:title IS NULL OR v.title = :title)
        AND (:after IS NULL OR v.date > :after)
        AND (:startDate IS NULL OR v.date >= :startDate)
        AND (:endDate IS NULL OR v.date <= :endDate)
    """)
    List<Video> searchVideos(String title, LocalDate after, LocalDate startDate, LocalDate endDate);

    Optional<Video> findByPublicId(String publicId);
}