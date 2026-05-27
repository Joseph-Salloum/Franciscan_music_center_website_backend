package music_center_backend.exception.exceptions;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException() {}

    public LessonNotFoundException(String message) {
        super(message);
    }
}
