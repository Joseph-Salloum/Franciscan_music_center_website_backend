package music_center_backend.exception.exceptions;

public class VideoNotFoundException extends RuntimeException {
    public VideoNotFoundException() {}

    public VideoNotFoundException(String message) {
        super(message);
    }
}
