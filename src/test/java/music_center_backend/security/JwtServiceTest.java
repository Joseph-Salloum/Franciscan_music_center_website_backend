package music_center_backend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import music_center_backend.config.JwtProperties;
import music_center_backend.security.service.JwtService;

class JwtServiceTest {

    private static final String SECRET = "01234567890123456789012345678901";

    private final JwtService jwtService = new JwtService(new JwtProperties(SECRET, 60_000L, 120_000L));

    @Test
    @DisplayName("generateToken should create a token whose subject can be extracted")
    void generateTokenShouldCreateTokenThatValidates() {
        String publicId = "user-123";

        String token = jwtService.generateToken(publicId);

        assertEquals(publicId, jwtService.validateAndExtractPublicId(token));
    }

    @Test
    @DisplayName("generateDevToken should create a token whose subject can be extracted")
    void generateDevTokenShouldCreateTokenThatValidates() {
        String publicId = "dev-user";

        String token = jwtService.generateDevToken(publicId);

        assertEquals(publicId, jwtService.validateAndExtractPublicId(token));
    }

    @Test
    @DisplayName("validateAndExtractPublicId should reject malformed tokens")
    void validateAndExtractPublicIdShouldRejectMalformedToken() {
        assertThrows(BadCredentialsException.class, () -> jwtService.validateAndExtractPublicId("not-a-jwt"));
    }

    @Test
    @DisplayName("validateAndExtractPublicId should reject tokens with a blank subject")
    void validateAndExtractPublicIdShouldRejectBlankSubject() {
        String blankSubjectToken = Jwts.builder()
                .setSubject("")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10_000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        assertThrows(BadCredentialsException.class, () -> jwtService.validateAndExtractPublicId(blankSubjectToken));
    }

    @Test
    @DisplayName("validateAndExtractPublicId should reject expired tokens")
    void validateAndExtractPublicIdShouldRejectExpiredToken() {
        String expiredToken = Jwts.builder()
                .setSubject("user-123")
                .setIssuedAt(new Date(System.currentTimeMillis() - 20_000))
                .setExpiration(new Date(System.currentTimeMillis() - 10_000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        assertThrows(BadCredentialsException.class, () -> jwtService.validateAndExtractPublicId(expiredToken));
    }
}