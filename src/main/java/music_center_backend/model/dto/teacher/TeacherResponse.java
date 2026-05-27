package music_center_backend.model.dto.teacher;

public class TeacherResponse {
    private final String publicId;
    private final String name;
    private final String specialization;
    private final boolean admin;

    public TeacherResponse(String publicId, String name, String specialization, boolean admin) {
        this.publicId = publicId;
        this.name = name;
        this.specialization = specialization;
        this.admin = admin;
    }

    public String getPublicId() { return this.publicId; }
    public String getName() { return this.name; }
    public String getSpecialization() { return this.specialization; }
    public boolean isAdmin() { return this.admin; }
}
