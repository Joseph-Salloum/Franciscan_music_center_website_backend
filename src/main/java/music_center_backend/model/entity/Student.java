package music_center_backend.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(nullable = false)
    private String name;

    @Column(name = "date_of_start", nullable = false)
    private LocalDate dateOfStart;

    @Column(nullable = false)
    private String instrument;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "taking_solfeige", nullable = false)
    private boolean takingSolfeige;

    @Column(name = "access_code_hash", nullable = false)
    private String accessCode;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<StudentMedal> studentMedals = new ArrayList<>();

    protected Student() {}

    public Student(String publicId, String name, LocalDate dateOfStart, String instrument, Teacher teacher, boolean takingSolfeige) {
        this.publicId = publicId;
        this.name = name;
        this.dateOfStart = (dateOfStart == null) ? LocalDate.now() : dateOfStart;
        this.instrument = instrument;
        this.teacher = teacher;
        this.takingSolfeige = takingSolfeige;
    }

    public Long getId() { return this.id; }
    public String getPublicId() { return this.publicId; }
    public String getName() { return this.name; }
    public LocalDate getDateOfStart() { return this.dateOfStart; }
    public String getInstrument() { return this.instrument; }
    public Teacher getTeacher() { return this.teacher; }
    public boolean isTakingSolfeige() { return this.takingSolfeige; }
    public String getAccessCode() { return this.accessCode; }

    public void setName(String name) { this.name = name; }
    public void setInstrument(String instrument) { this.instrument = instrument; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    public void setTakingSolfeige(boolean takingSolfeige) { this.takingSolfeige = takingSolfeige; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }
}
