package music_center_backend.exception.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {}

    public UserNotFoundException(String message) {
        super(message);
    }
}
