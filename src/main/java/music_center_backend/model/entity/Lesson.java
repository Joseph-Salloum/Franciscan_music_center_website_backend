package music_center_backend.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import music_center_backend.model.constants.State;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @Column(name = "mark", nullable = false)
    private short mark;

    @Column(name = "note")
    private String note;

    @Column(name = "instrument", nullable = false)
    private boolean instrument;

    protected Lesson() {}

    public Lesson(String publicId, LocalDate date, Student student, Teacher teacher, State state, short mark, String note, boolean instrument) {
        this.publicId = publicId;
        this.date = (date == null) ? LocalDate.now() : date;
        this.student = student;
        this.teacher = teacher;
        this.state = state;
        this.mark = mark;
        this.note = note;
        this.instrument = instrument;
    }

    public Long getId() { return this.id; }
    public String getPublicId() { return this.publicId; }
    public LocalDate getDate() { return this.date; }
    public Student getStudent() { return this.student; }
    public Teacher getTeacher() { return this.teacher; }
    public State getState() { return this.state; }
    public short getMark() { return this.mark; }
    public String getNote() { return this.note; }
    public boolean isInstrument() { return this.instrument; }

    public void setState(State state) { this.state = state; }
    public void setMark(short mark) { this.mark = mark; }
    public void setNote(String note) { this.note = note; }
}
