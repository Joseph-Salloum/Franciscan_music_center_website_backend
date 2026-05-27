package music_center_backend.exception.exceptions;

public class MedalNotFoundException extends RuntimeException {
    public MedalNotFoundException() {}

    public MedalNotFoundException(String message) {
        super(message);
    }
}
