package music_center_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import music_center_backend.model.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByPublicId(String publicId);
    List<Student> findByTeacherPublicId(String teacherPublicId);
    boolean existsByPublicIdAndTeacher_PublicId(String studentPublicId, String teacherPublicId);
}
