package music_center_backend.model.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String publicId;

    @NotBlank
    private String accessCode;

    public LoginRequest() {}

    public String getPublicId() { return this.publicId; }
    public String getAccessCode() { return this.accessCode; }

    public void setPublicId(String publicId) { this.publicId = publicId; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }
}
