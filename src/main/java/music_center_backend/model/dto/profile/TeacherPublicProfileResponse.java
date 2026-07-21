package music_center_backend.model.dto.profile;

public class TeacherPublicProfileResponse {
    private String name;
    private String specialization;
    private boolean admin;

    public TeacherPublicProfileResponse(String name, String specialization, boolean admin) {
        this.name = name;
        this.specialization = specialization;
        this.admin = admin;
    }

    public String getName() { return this.name; }
    public String getSpecialization() { return this.specialization; }
    public boolean isAdmin() { return this.admin; }
}
