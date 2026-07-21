package music_center_backend.security.config;

public final class SecurityEndpoints {
    public static final String[] PUBLIC_ENDPOINT_PATTERNS = {
        "/auth/**",
        "/api/v1/dashboard/**"
    };

    private SecurityEndpoints() {}
}
