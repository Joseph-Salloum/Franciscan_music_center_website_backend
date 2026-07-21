package music_center_backend.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "app.jwt")
@Validated
public class JwtProperties {
    @NotBlank
    private final String secret;

    @NotNull @Min(600000)
    private final Long expirationMs;

    @NotNull
    private final Long devExpirationMs;

    public JwtProperties(String secret, Long expirationMs, Long devExpirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
        this.devExpirationMs = devExpirationMs;
    }

    public String getSecret() { return this.secret; }
    public Long getExpirationMs() { return this.expirationMs; }
    public Long getDevExpirationMs() { return this.devExpirationMs; }
}
