package music_center_backend.exception.exceptions;

public class DuplicateMedalNameException extends RuntimeException {
    public DuplicateMedalNameException() {}

    public DuplicateMedalNameException(String message) {
        super(message);
    }
}
