package music_center_backend.model.dto.auth;

public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() { return this.token; }
}
