package music_center_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import music_center_backend.model.entity.Medal;

public interface MedalRepository extends JpaRepository<Medal, Long> {
    Optional<Medal> findByMedalName(String name);
}
