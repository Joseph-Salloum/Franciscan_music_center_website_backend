package music_center_backend.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "medals")
public class Medal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medal_name", nullable = false)
    private String medalName;

    @Column(name = "medal_description", nullable = false)
    private String medalDescription;

    @OneToMany(mappedBy = "medal", fetch = FetchType.LAZY)
    private List<StudentMedal> studentMedals = new ArrayList<>();

    protected Medal() {}

    public Medal(String medalName, String medalDescription) {
        this.medalName = medalName;
        this.medalDescription = medalDescription;
    }

    public Long getId() { return this.id; }
    public String getMedalName() { return this.medalName; }
    public String getMedalDescription() { return this.medalDescription; }

    public void setMedalName(String medalName) { this.medalName = medalName; }
    public void setMedalDescription(String medalDescription) { this.medalDescription = medalDescription; }
}
