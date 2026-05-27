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
@Table(name = "marks")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "mark", nullable = false)
    private short mark;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "instrument", nullable = false)
    private boolean instrument;

    protected Mark() {}

    public Mark(String publicId, Student student, Teacher teacher, short mark, LocalDate date, boolean instrument) {
        this.publicId = publicId;
        this.student = student;
        this.teacher = teacher;
        this.mark = mark;
        this.date = (date == null) ? LocalDate.now() : date;
        this.instrument = instrument;
    }

    public Long getId() { return this.id; }
    public String getPublicId() { return this.publicId; }
    public Student getStudent() { return this.student; }
    public Teacher getTeacher() { return this.teacher; }
    public short getMark() { return this.mark; }
    public LocalDate getDate() { return this.date; }
    public boolean isInstrument() { return this.instrument; }

    public void setMark(short mark) { this.mark = mark; }
}
