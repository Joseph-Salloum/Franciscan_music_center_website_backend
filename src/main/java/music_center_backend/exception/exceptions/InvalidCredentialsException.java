package music_center_backend.exception.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {}

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
