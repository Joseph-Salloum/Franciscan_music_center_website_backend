package music_center_backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import music_center_backend.model.constant.Specialization;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization specialization;

    @Column(name = "is_admin", nullable = false)
    private boolean admin;

    @Column(name = "access_code_hash", nullable = false)
    private String accessCode;

    protected Teacher() {}

    public Teacher(String publicId, String name, Specialization specialization, boolean isAdmin) {
        this.publicId = publicId;
        this.name = name;
        this.specialization = specialization;
        this.admin = isAdmin;
    }

    public Long getId() { return this.id; }
    public String getPublicId() { return this.publicId; }
    public String getName() { return this.name; }
    public Specialization getSpecialization() { return this.specialization; }
    public boolean isAdmin() { return this.admin; }
    public String getAccessCode() { return this.accessCode; }

    public void setName(String name) { this.name = name; }
    public void setSpecialization(Specialization specialization) { this.specialization = specialization; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }
    public void setAdmin(boolean admin) { this.admin = admin; }
}
