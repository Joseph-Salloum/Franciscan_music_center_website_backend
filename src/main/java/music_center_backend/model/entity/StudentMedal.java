package music_center_backend.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "students_medals")
public class StudentMedal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medal_id", nullable = false)
    private Medal medal;

    @Column(name = "medal_date", nullable = false)
    private LocalDate medalDate;

    protected StudentMedal() {}

    public StudentMedal(Student student, Medal medal, LocalDate medalDate) {
        this.student = student;
        this.medal = medal;
        this.medalDate = medalDate;
    }

    public Long getId() { return this.id; }
    public Student getStudent() { return this.student; }
    public Medal getMedal() { return this.medal; }
    public LocalDate getMedalDate() { return this.medalDate; }
}
