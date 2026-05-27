package music_center_backend.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;

public final class HashGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomHash(int length) {
        try {
            byte[] randomBytes = new byte[32];
            RANDOM.nextBytes(randomBytes);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(randomBytes);

            String hex = HexFormat.of().formatHex(hash);

            return hex.substring(0, length);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
