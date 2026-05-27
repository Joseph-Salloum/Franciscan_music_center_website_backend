package music_center_backend.exception.exceptions;

public class MalformedIdException extends RuntimeException {
    public MalformedIdException() {}

    public MalformedIdException(String message) {
        super(message);
    }
}
